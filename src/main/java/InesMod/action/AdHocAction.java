package InesMod.action;

import InesMod.patchs.InPlayerEndTurnPeriodManager;
import InesMod.powers.player.AdHocStrategyPower;
import InesMod.powers.player.AdHocSupplyPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 临时战略 和 临时补给 的效果
 */
public class AdHocAction extends AbstractGameAction {
    private final AbstractCreature owner;



    public AdHocAction(AbstractCreature owner) {
        this.owner = owner;
    }

    public void update() {
        if (owner instanceof AbstractPlayer
                && ((AbstractPlayer)owner).hand.size() <= 1
                && !InPlayerEndTurnPeriodManager.inPlayerEndTurnPeriod // 未处于回合结束
        )
        {
            AbstractPower adHocStrategyPower = owner.getPower(AdHocStrategyPower.ID);
            if (adHocStrategyPower != null){
                adHocStrategyPower.flash();

                // 抽牌
                addToTop(new DrawCardAction(owner, adHocStrategyPower.amount));
                addToTop(new ReducePowerAction(owner, owner, AdHocStrategyPower.ID, adHocStrategyPower.amount));

            }

            AbstractPower adHocSupplyPower = owner.getPower(AdHocSupplyPower.ID);
            if (adHocSupplyPower != null){
                adHocSupplyPower.flash();

                // 获得能量
                addToTop(new GainEnergyAction(adHocSupplyPower.amount));
                addToTop(new ReducePowerAction(owner, owner, AdHocSupplyPower.ID, adHocSupplyPower.amount));
            }
        }




        this.isDone = true;
    }

}
