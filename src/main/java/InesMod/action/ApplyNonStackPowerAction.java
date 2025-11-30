package InesMod.action;

import InesMod.powers.QuickVictoryPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 获得 不可叠加power 的 action
 */
public class ApplyNonStackPowerAction extends AbstractGameAction {
    AbstractPower powerToApply;

    public ApplyNonStackPowerAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply) {
        this.target = target;
        this.source = source;
        this.powerToApply = powerToApply;
    }
    public void update() {
        AbstractPower powerToGet = target.getPower(powerToApply.ID);
        if (powerToGet == null) {
            addToTop(new ApplyPowerAction(target, source, powerToApply));
        }
        this.isDone = true;
    }
}