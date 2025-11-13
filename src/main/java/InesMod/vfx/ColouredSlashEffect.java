package InesMod.vfx;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;

public class ColouredSlashEffect extends AbstractGameAction {
    private final float angle;
    private final float targetScale;
    private final Color color1;
    private final Color color2;



    public ColouredSlashEffect(AbstractMonster m, float angle, float targetScale, Color color1, Color color2) {
        this.source = m;
        this.angle = angle;
        this.targetScale = targetScale;
        this.color1 = color1;
        this.color2 = color2;
    }


    public void update() {
        if (this.source.isDeadOrEscaped()) {
            this.isDone = true;
            return;
        }

        float len = 500.0f; // 长度
        float rad = MathUtils.degreesToRadians * angle; // 转弧度
        float dx = len * MathUtils.cos(rad);
        float dy = - len * MathUtils.sin(rad);

        this.addToTop(new VFXAction(new AnimatedSlashEffect(
                this.source.hb.cX,
                this.source.hb.cY - 30.0F * Settings.scale,
                dx, dy,
                this.angle,
                this.targetScale,
                this.color1.cpy(),
                this.color2.cpy()
        )));
        this.isDone = true;
    }
}
