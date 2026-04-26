package InesMod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

/**
 * 边框缓慢的闪烁特效
 */
public class BorderBreathingEffect extends AbstractGameEffect {
    private TextureAtlas.AtlasRegion img;

    private final float durationTotal;
    private final float maxAlpha; // 透明度
    private final boolean additive;  // 是否使用叠加混合模式





    public BorderBreathingEffect(Color color, float maxAlpha, float duration, boolean additive) {
        this.img = ImageMaster.BORDER_GLOW_2;
        this.durationTotal = duration; // 记录总时长
        this.duration = duration;      // 初始化 AbstractGameEffect 的 duration
        this.maxAlpha = maxAlpha;      // 记录最大透明度
        this.color = color.cpy();
        this.color.a = 0.0F;
        this.additive = additive;
    }

    public void update() {
        // 修改：使用实例变量 durationTotal 来进行对称的插值计算
        // 前半段（duration > 总时长的一半）：逐渐出现
        if (this.duration > durationTotal / 2) {
            // 计算前半段进度 (0.0 -> 1.0)，并乘以最大透明度
            float progress = (durationTotal - this.duration) / (durationTotal / 2);
            this.color.a = Interpolation.fade.apply(0.0F, maxAlpha, progress);
        }
        // 后半段（duration <= 总时长的一半）：逐渐消失
        else {
            // 计算后半段进度 (1.0 -> 0.0)，并乘以最大透明度
            float progress = this.duration / (durationTotal / 2);
            this.color.a = Interpolation.fade.apply(0.0F, maxAlpha, progress);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        if (this.additive) {
            sb.setBlendFunction(770, 1);
            sb.setColor(this.color);
            sb.draw(this.img, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
            sb.setBlendFunction(770, 771);
        } else {
            sb.setColor(this.color);
            sb.draw(this.img, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
        }
    }

    public void dispose() {
    }
}