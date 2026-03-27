package InesMod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.GenericSmokeEffect;

public class DefenseArtilleryFireEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private static final float DUR = 0.6F;
    private TextureAtlas.AtlasRegion img;
    private boolean playedSound = false;

    public DefenseArtilleryFireEffect(float x, float y) {
        this.img = ImageMaster.VERTICAL_IMPACT;
        // 调整位置，确保中心点大致对齐
        this.x = x - (float)this.img.packedWidth / 2.0F;
        this.y = y - (float)this.img.packedHeight * 0.01F;
        this.startingDuration = 0.6F;
        this.duration = 0.6F;
        this.scale = Settings.scale;

        // 初始旋转角度增加 90 度
        this.rotation = MathUtils.random(40.0F, 50.0F) - 90.0F;

        this.color = Color.SCARLET.cpy();
        this.renderBehind = false;

        for(int i = 0; i < 50; ++i) {
            AbstractDungeon.effectsQueue.add(new GenericSmokeEffect(x + MathUtils.random(-280.0F, 250.0F) * Settings.scale, y - 80.0F * Settings.scale));
        }

    }

    private void playRandomSfX() {
        CardCrawlGame.sound.playA("BLUNT_HEAVY", -0.3F);
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

        if (this.duration < 0.5F && !this.playedSound) {
            this.playRandomSfX();
            this.playedSound = true;
        }

        if (this.duration > 0.2F) {
            this.color.a = Interpolation.fade.apply(0.5F, 0.0F, (this.duration - 0.34F) * 5.0F);
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 0.5F, this.duration * 5.0F);
        }

        // 增大基础缩放值，从 1.1F/1.05F 增大到 1.5F/1.4F
        this.scale = Interpolation.fade.apply(Settings.scale * 1.5F, Settings.scale * 1.4F, this.duration / 0.6F);
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        // 增大所有 sb.draw 的缩放系数
        // 例如，将 this.scale * 0.3F 改为 this.scale * 0.5F
        sb.draw(this.img, this.x + MathUtils.random(-10.0F, 10.0F) * Settings.scale, this.y, (float)this.img.packedWidth / 2.0F, 0.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * 0.5F, this.scale * 1.0F, this.rotation + 18.0F);
        sb.draw(this.img, this.x + MathUtils.random(-10.0F, 10.0F) * Settings.scale, this.y, (float)this.img.packedWidth / 2.0F, 0.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * 0.5F, this.scale * 1.0F, this.rotation - MathUtils.random(12.0F, 18.0F));
        sb.draw(this.img, this.x + MathUtils.random(-10.0F, 10.0F) * Settings.scale, this.y, (float)this.img.packedWidth / 2.0F, 0.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * 0.6F, this.scale * 0.7F, this.rotation + MathUtils.random(-10.0F, 14.0F));
        sb.draw(this.img, this.x + MathUtils.random(-10.0F, 10.0F) * Settings.scale, this.y, (float)this.img.packedWidth / 2.0F, 0.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * 0.9F, this.scale * 1.1F, this.rotation - MathUtils.random(20.0F, 28.0F));
        // 最大的一个，从 1.5F 增大到 2.0F
        sb.draw(this.img, this.x + MathUtils.random(-10.0F, 10.0F) * Settings.scale, this.y, (float)this.img.packedWidth / 2.0F, 0.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * 2.0F, this.scale * MathUtils.random(1.8F, 2.0F), this.rotation);

        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, true);

        Color c = Color.GOLD.cpy();
        c.a = this.color.a;
        sb.setColor(c);
        // 同样增大金色部分的缩放
        sb.draw(this.img, this.x + MathUtils.random(-10.0F, 10.0F) * Settings.scale, this.y, (float)this.img.packedWidth / 2.0F, 0.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * 1.2F, this.scale * MathUtils.random(1.0F, 1.4F), this.rotation);
        sb.draw(this.img, this.x + MathUtils.random(-10.0F, 10.0F) * Settings.scale, this.y, (float)this.img.packedWidth / 2.0F, 0.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * 1.2F, this.scale * MathUtils.random(0.6F, 0.8F), this.rotation);
        sb.draw(this.img, this.x + MathUtils.random(-10.0F, 10.0F) * Settings.scale, this.y, (float)this.img.packedWidth / 2.0F, 0.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * 0.7F, this.scale * 0.9F, this.rotation - MathUtils.random(20.0F, 28.0F));
        sb.setBlendFunction(770, 771);

        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, true);

    }

    public void dispose() {
    }
}