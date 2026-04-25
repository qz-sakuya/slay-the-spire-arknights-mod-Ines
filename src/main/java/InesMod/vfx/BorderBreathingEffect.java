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
    // 修改1：将总持续时间延长到 2.0 秒，让出现和消失都更慢、更从容
    private static final float DUR = 2.0F;
    // 限制最大透明度（你可以根据需要调整这个数值）
    private static final float MAX_ALPHA = 0.5F;
    private boolean additive;

    public BorderBreathingEffect(Color color) {
        this(color, true);
    }

    public BorderBreathingEffect(Color color, boolean additive) {
        this.img = ImageMaster.BORDER_GLOW_2;
        this.duration = DUR; // 使用定义的 DUR 常量
        this.color = color.cpy();
        this.color.a = 0.0F;
        this.additive = additive;
    }

    public void update() {
        // 修改2：使用对称的插值逻辑，让出现和消失的速率完全一致
        // 前半段（duration > 1.0F）：逐渐出现
        if (this.duration > 1.0F) {
            // 计算前半段进度 (0.0 -> 1.0)，并乘以最大透明度
            float progress = (DUR - this.duration);
            this.color.a = Interpolation.fade.apply(0.0F, MAX_ALPHA, progress);
        }
        // 后半段（duration <= 1.0F）：逐渐消失
        else {
            // 计算后半段进度 (1.0 -> 0.0)，并乘以最大透明度
            float progress = this.duration;
            this.color.a = Interpolation.fade.apply(0.0F, MAX_ALPHA, progress);
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
