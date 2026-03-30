package InesMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 触发power SpecificTrigger 回调的action
 */
public class SpecificTriggerPowerAction extends AbstractGameAction {
    private final AbstractPower power;

    public SpecificTriggerPowerAction(AbstractPower power) {
        this.power = power;
    }

    public void update() {
        power.onSpecificTrigger();

        this.isDone = true;
    }
}
