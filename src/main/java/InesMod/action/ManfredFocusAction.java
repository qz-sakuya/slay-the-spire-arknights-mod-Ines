package InesMod.action;

import InesMod.helpers.LogHelper;
import InesMod.powers.monster.ManfredFocusPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

/**
 * 专注power 的效果
 */
public class ManfredFocusAction extends AbstractGameAction {
    AbstractCreature owner;

    public ManfredFocusAction(AbstractCreature owner) {
        this.owner = owner;
    }

    @Override
    public void update() {
        AbstractPower manfredFocusPower = owner.getPower(ManfredFocusPower.ID);
        if (manfredFocusPower instanceof ManfredFocusPower) {
            int strengthAmtToChange = ((ManfredFocusPower)manfredFocusPower).strengthAmtToChange;

            if (strengthAmtToChange < 0) { // 减少力量
                AbstractPower strengthPower = owner.getPower(StrengthPower.POWER_ID);
                if (strengthPower != null && strengthPower.amount > 0) { // 有力量才能减
                    if (strengthPower.amount + strengthAmtToChange < 0) {
                        strengthAmtToChange = strengthPower.amount;
                    }
                }
                else{
                    strengthAmtToChange = 0;
                }
            }

            if (strengthAmtToChange != 0) {
                LogHelper.info("===ManfredFocusAction：onInflictDamage：修改力量：{}===", strengthAmtToChange);

                manfredFocusPower.flash();
                addToTop(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strengthAmtToChange), strengthAmtToChange));
            }


            ((ManfredFocusPower)manfredFocusPower).strengthAmtToChange = 0;
        }



        this.isDone = true;
    }

}
