package InesMod.vfx;


import InesMod.modcore.InesModMain;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import java.util.ArrayList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * 牵引投掷 的特效
 */
public class GuidedThrowEffect extends AbstractGameEffect {
    private static TextureAtlas.AtlasRegion img;
    private float sX;
    private float sY;
    private float cX;
    private float cY;
    private ArrayList<Vector2> previousPos = new ArrayList<>();
    private float dX;
    private float dY;
    private float yOffset;
    private float bounceHeight;
    private static final float DUR = 0.6F;
    private boolean playedSfx = false;

    public GuidedThrowEffect(float srcX, float srcY, float destX, float destY) {
        if (img == null) {
             img = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("InesModResources/img/vfx/GuidedThrow.png"), 0, 0, 100, 100);
        }

        this.sX = srcX;
        this.sY = srcY;
        this.cX = this.sX;
        this.cY = this.sY;
        this.dX = destX;
        this.dY = destY;
        this.rotation = 0.0F;
        this.duration = 0.6F;

        this.color = InesModMain.MY_COLOR_DARK;

        float baseBounce = 400.0F; // 弹跳高度
        if (this.sY > this.dY) {
            this.bounceHeight = baseBounce * Settings.scale;
        } else {
            this.bounceHeight = this.dY - this.sY + baseBounce * Settings.scale;
        }
    }

    public void update() {
        this.cX = Interpolation.linear.apply(this.dX, this.sX, this.duration / 0.6F);
        this.cY = Interpolation.linear.apply(this.dY, this.sY, this.duration / 0.6F);

        this.previousPos.add(new Vector2(this.cX +
                MathUtils.random(-30.0F, 30.0F) * Settings.scale, this.cY + this.yOffset +
                MathUtils.random(-30.0F, 30.0F) * Settings.scale));
        if (this.previousPos.size() > 20) {
            this.previousPos.remove(this.previousPos.get(0));
        }

        // 统一确定旋转方向
        float baseRotationSpeed = (this.dX > this.sX) ? -1000.0f : 1000.0f;

        // 原始旋转
        this.rotation += baseRotationSpeed * Gdx.graphics.getDeltaTime();

        // ========== 额外增加 720° 的旋转（2圈），方向与飞行方向一致 ==========
        float extraRotationTotal = 720.0f;
        float extraRotationSpeed = extraRotationTotal / 0.6f; // ~1200 deg/s
        this.rotation += baseRotationSpeed > 0 ? extraRotationSpeed * Gdx.graphics.getDeltaTime()
                : -extraRotationSpeed * Gdx.graphics.getDeltaTime();

        if (this.duration > 0.3F) {
            this.color.a = Interpolation.exp5In.apply(1.0F, 0.0F, (this.duration - 0.3F) / 0.3F) * Settings.scale;
            this.yOffset = Interpolation.circleIn.apply(this.bounceHeight, 0.0F, (this.duration - 0.3F) / 0.3F) * Settings.scale;
        } else {
            this.yOffset = Interpolation.circleOut.apply(0.0F, this.bounceHeight, this.duration / 0.3F) * Settings.scale;
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        // ========== Step 1: 绘制基础层 - 显示原始图片 ==========
        // 使用 WHITE 颜色，让纹理显示其原始 RGB 色彩
        // 透明度 (alpha) 仍然由动画的 duration 控制
        float baseAlpha = Interpolation.fade.apply(0.7F, 1.0F, this.duration / DUR); // 开始时稍透明，中间最亮
        sb.setColor(Color.WHITE.cpy().set(1.0F, 1.0F, 1.0F, baseAlpha));

        // 绘制原图（核心物体）
        sb.draw(img,
                this.cX - img.packedWidth / 2,
                this.cY - img.packedHeight / 2 + this.yOffset,
                img.packedWidth / 2.0F, img.packedHeight / 2.0F,
                img.packedWidth, img.packedHeight,
                this.scale * 1.1f, this.scale * 1.1f, // 稍微大一点，给外框留空间
                this.rotation);

        // ========== Step 2: 绘制高光/发光边缘 (使用叠加混合) ==========
        sb.setBlendFunction(770, 1); // SrcAlpha, One (Additive Blend)

        // 发光边缘颜色：使用你的主题色，但更亮一些
        Color glowColor = InesModMain.MY_COLOR_DARK.cpy();
        glowColor.r = Math.min(glowColor.r * 1.5f, 1.0f);
        glowColor.g = Math.min(glowColor.g * 1.5f, 1.0f);
        glowColor.b = Math.min(glowColor.b * 1.5f, 1.0f);
        glowColor.a = baseAlpha * 0.8f; // 控制发光强度

        sb.setColor(glowColor);
        // 绘制一个略大的版本作为发光边
        sb.draw(img,
                this.cX - img.packedWidth / 2,
                this.cY - img.packedHeight / 2 + this.yOffset,
                img.packedWidth / 2.0F, img.packedHeight / 2.0F,
                img.packedWidth, img.packedHeight,
                this.scale * 1.3f, this.scale * 1.3f,
                this.rotation);

        // ========== Step 3: 绘制拖尾/轨迹粒子 (POWER_UP_2 光斑) ==========
        // 使用更小、更透明的光斑形成轨迹
        sb.setColor(new Color(0.6F, 1.0F, 1.0F, baseAlpha * 0.4f)); // 青白色光斑

        for (int i = 5; i < this.previousPos.size(); i++) {
            float sizeFactor = 1.0f - (i / (float)this.previousPos.size()); // 越老的点越小越淡
            float s = this.scale * 0.3f * sizeFactor;

            sb.draw(ImageMaster.POWER_UP_2,
                    this.previousPos.get(i).x - ImageMaster.POWER_UP_2.packedWidth / 2,
                    this.previousPos.get(i).y - ImageMaster.POWER_UP_2.packedHeight / 2,
                    ImageMaster.POWER_UP_2.packedWidth / 2.0F,
                    ImageMaster.POWER_UP_2.packedHeight / 2.0F,
                    ImageMaster.POWER_UP_2.packedWidth,
                    ImageMaster.POWER_UP_2.packedHeight,
                    s, s, this.rotation * 0.5f);
        }

        // ========== Step 4: 恢复默认混合模式 ==========
        sb.setBlendFunction(770, 771); // Normal alpha blending
    }

    public void dispose() {}
}