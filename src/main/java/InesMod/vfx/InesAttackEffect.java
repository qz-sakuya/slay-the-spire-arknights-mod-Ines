package InesMod.vfx;


import InesMod.action.ForceWaitAction;
import InesMod.characters.Ines;
import InesMod.helpers.LogHelper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;

public class InesAttackEffect extends AbstractGameAction {
    public final int type;
    public float delay = 0.0F;



    public InesAttackEffect(AbstractCreature source, int type, float delay, float delayTimeScale) {
        this.source = source;
        this.type = type;
        this.delay = delay / delayTimeScale;
    }

    public InesAttackEffect(AbstractCreature source, int type, float delayTimeScale) {
        this(source, type, 0, delayTimeScale);

        if (source instanceof Ines) {
            delay = ((Ines) source).getAttackAnimationDelay(type) / delayTimeScale;
        }
    }

    public InesAttackEffect(AbstractCreature source, int type) {
        this(source, type, 1F);
    }


    public void update() {
        LogHelper.info("===Ines:InesAttackEffect：开始：type={}===",type);
        if (source instanceof Ines) {
            ((Ines) source).playAttackAnimation(type);
        }

        addToTop(new ForceWaitAction(delay));



        LogHelper.info("===Ines:InesAttackEffect：结束：type={}===",type);
        this.isDone = true;
    }
}
