package InesMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 添加 不可叠加power 的 action
 * 仅当目标没有该 power 时才添加
 * powerToApply 仍允许有层数
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