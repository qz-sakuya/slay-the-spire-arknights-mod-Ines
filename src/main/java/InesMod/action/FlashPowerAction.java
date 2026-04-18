package InesMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 触发power flash 的action
 */
public class FlashPowerAction extends AbstractGameAction {
    private final AbstractPower power;

    public FlashPowerAction(AbstractPower power) {
        this.power = power;
    }

    public void update() {
        power.flash();

        this.isDone = true;
    }
}
