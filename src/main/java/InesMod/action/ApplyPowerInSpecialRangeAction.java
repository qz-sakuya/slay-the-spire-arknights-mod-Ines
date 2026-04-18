package InesMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 在目标 power 的层数处于特定范围（包括端点值）时才添加
 * 添加后，也不超过范围
 * 目标 power 不存在视为0层
 */
public class ApplyPowerInSpecialRangeAction extends AbstractGameAction {
    AbstractPower powerToApply;
    int amountToApply;
    int minAmountLimit;
    int maxAmountLimit;

    

    public ApplyPowerInSpecialRangeAction(AbstractCreature target,
                                          AbstractCreature source,
                                          AbstractPower powerToApply,
                                          int amountToApply,
                                          int minAmountLimit,
                                          int maxAmountLimit
    ) {
        this.target = target;
        this.source = source;
        this.powerToApply = powerToApply;
        this.amountToApply = amountToApply;
        this.minAmountLimit = minAmountLimit;
        this.maxAmountLimit = maxAmountLimit;
    }
    public void update() {
        int currentAmount = 0;
        
        AbstractPower powerToGet = target.getPower(powerToApply.ID);
        if (powerToGet != null) {
            currentAmount = powerToGet.amount;
        }



        // 叠加
        if (currentAmount >= minAmountLimit && currentAmount <= maxAmountLimit) {
            // 不超过范围
            if (powerToGet != null) {
                if (amountToApply > maxAmountLimit - currentAmount) {
                    amountToApply = maxAmountLimit - currentAmount;
                }
                if (amountToApply < minAmountLimit - currentAmount) {
                    amountToApply = minAmountLimit - currentAmount;
                }

                if (amountToApply == 0) {
                    this.isDone = true;
                    return;
                }
            }
            else {
                if (powerToApply.amount > maxAmountLimit) {
                    powerToApply.amount = maxAmountLimit;
                }
                if (powerToApply.amount < minAmountLimit) {
                    powerToApply.amount = minAmountLimit;
                }
            }

            addToTop(new ApplyPowerAction(target, source, powerToApply, amountToApply));
        }
        
        
        this.isDone = true;
    }
}