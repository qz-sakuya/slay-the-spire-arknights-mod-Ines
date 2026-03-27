package InesMod.vfx;


import InesMod.action.ForceWaitAction;
import InesMod.characters.Ines;
import InesMod.helpers.LogHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class TopSecretOperationEffect1 extends AbstractGameAction {
    public TopSecretOperationEffect1(AbstractCreature source) {
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
