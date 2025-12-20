package InesMod.vfx;


import InesMod.modcore.InesModMain;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

/**
 * 牵引投掷 的特效
 */
public class OnTheBrinkEffect extends AbstractGameEffect {
    private static TextureAtlas.AtlasRegion img;
    private float sX; // 起始 X（固定 = dX）
    private float sY; // 起始 Y（目标上方）
    private float dX; // 目标 X
    private float dY; // 目标 Y
    private float cX; // 当前 X（始终 = dX）
    private float cY; // 当前 Y（随时间下落）

    private static final float DUR = 0.6F;
    private static final float DROP_HEIGHT = 300.0F * Settings.scale; // 下落高度（可调）

    public OnTheBrinkEffect(float destX, float destY, float dropHeight) {
        if (img == null) {
            img = new TextureAtlas.AtlasRegion(
                    ImageMaster.loadImage("InesModResources/img/vfx/GuidedThrow.png"), 0, 0, 100, 100);
        }

        this.dX = destX;
        this.dY = destY;
        this.sX = destX; // 垂直下落 → X 不变
        this.sY = destY + dropHeight; // 关键：使用传入的 dropHeight

        this.cX = this.sX;
        this.cY = this.sY;

        this.duration = DUR;
        this.color = InesModMain.MY_COLOR_DARK.cpy();
        this.rotation = 0.0F;
        this.scale = 1.0F;
    }

    @Override
    public void update() {
        // 【核心】垂直下落：sY → dY，使用 pow2In 实现加速感（比 linear 更自然）
        float t = 1.0F - this.duration / DUR; // 归一化进度 [0→1]
        this.cY = Interpolation.pow2In.apply(this.sY, this.dY, t);

        // 可选：轻微随机摇晃增强动感（取消注释启用）
        // this.rotation += (MathUtils.randomBoolean() ? 1.0f : -1.0f) * 150f * Gdx.graphics.getDeltaTime();

        // 透明度：开始淡入 + 结束淡出（更专业）
        float alphaProgress = t;
        if (alphaProgress < 0.2f) {
            this.color.a = Interpolation.fade.apply(0.0f, 1.0f, alphaProgress / 0.2f);
        } else if (alphaProgress > 0.8f) {
            this.color.a = Interpolation.fade.apply(1.0f, 0.0f, (alphaProgress - 0.8f) / 0.2f);
        } else {
            this.color.a = 1.0f;
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration <= 0.0F) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        // 【1】预计算共用参数
        float x = this.cX - img.packedWidth / 2;
        float y = this.cY - img.packedHeight / 2;
        float originX = img.packedWidth / 2.5F;
        float originY = img.packedHeight / 2.5F;
        float baseScale = 2.0f;

        // 【2】绘制发光轮廓层（3层，由外向内：更大 → 更小，更淡 → 更亮）
        // 使用纯白色（R=G=B=1），Alpha 随层递增（外层最淡，内层最亮），模拟高斯衰减感
        float[] glowScales = {2.3f, 2.15f, 2.05f}; // 比主图略大
        float[] glowAlphas = {0.25f, 0.4f, 0.6f};  // 外→内渐亮

        Color white = Color.WHITE.cpy();
        for (int i = 0; i < glowScales.length; i++) {
            float scale = glowScales[i];
            white.a = this.color.a * glowAlphas[i]; // 叠加主图当前透明度（淡入/淡出同步）
            sb.setColor(white);
            sb.draw(img,
                    x, y,
                    originX, originY,
                    img.packedWidth, img.packedHeight,
                    scale, scale,
                    -90.0f);
        }

        // 【3】绘制主图（原始逻辑，但确保颜色含 alpha）
        sb.setColor(this.color); // 此时 color.a 已在 update() 中更新（淡入/淡出）
        sb.draw(img,
                x, y,
                originX, originY,
                img.packedWidth, img.packedHeight,
                baseScale, baseScale,
                -90.0f);
    }

    @Override
    public void dispose() {
        // 无需释放贴图（ImageMaster 管理），留空即可
    }
}