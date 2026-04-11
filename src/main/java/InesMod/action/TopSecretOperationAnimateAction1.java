package InesMod.action;


import InesMod.characters.Ines;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class TopSecretOperationAnimateAction1 extends AbstractGameAction {
    public TopSecretOperationAnimateAction1(AbstractCreature source) {
        this.source = source;
    }


    public void update() {
        if (source instanceof Ines) {
            Ines ines = (Ines) source;
            ines.state.setAnimation(0, "Skill_2_Begin", false);
            ines.state.addAnimation(0, "Skill_2_Attack", false, 0.0F);

            ines.state.getCurrent(0).setTimeScale(1.2F);

            float delay = ines.getAttackAnimationDelay(2);
            delay -= 0.1F;
            addToTop(new ForceWaitAction(delay));
        }




        this.isDone = true;
    }
}
