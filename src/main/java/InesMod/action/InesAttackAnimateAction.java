package InesMod.action;


import InesMod.characters.Ines;
import InesMod.helpers.LogHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class InesAttackAnimateAction extends AbstractGameAction {
    public final int type;
    public float delay = 0.0F;



    public InesAttackAnimateAction(AbstractCreature source, int type, float delay, float delayTimeScale) {
        this.source = source;
        this.type = type;
        this.delay = delay / delayTimeScale;
    }

    public InesAttackAnimateAction(AbstractCreature source, int type, float delayTimeScale) {
        this(source, type, 0, delayTimeScale);

        if (source instanceof Ines) {
            delay = ((Ines) source).getAttackAnimationDelay(type) / delayTimeScale;
        }
    }

    public InesAttackAnimateAction(AbstractCreature source, int type) {
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
