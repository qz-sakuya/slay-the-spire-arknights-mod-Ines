package InesMod.action;


import InesMod.characters.Ines;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class TopSecretOperationAnimateAction2 extends AbstractGameAction {
    public TopSecretOperationAnimateAction2(AbstractCreature source) {
        this.source = source;
    }


    public void update() {
        if (source instanceof Ines) {
            Ines ines = (Ines) source;
            ines.state.setAnimation(0, "Skill_2_Attack", false);
            ines.state.addAnimation(0, "Skill_2_End", false, 0.0F);

            ines.state.addAnimation(0, "Idle", true, 0.0F);
            ines.state.getCurrent(0).setTimeScale(1.2F);

        }




        this.isDone = true;
    }
}
