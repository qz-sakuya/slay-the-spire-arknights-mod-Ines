package InesMod.action;

import InesMod.cards.skill.AdHocStrategy;
import InesMod.powers.AdHocStrategyPower;
import InesMod.powers.AdHocSupplyPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 临时战略 的效果
 */
public class AdHocStrategyAction extends AbstractGameAction {
    private final AbstractCreature owner;
    private final int amount;


    public AdHocStrategyAction(AbstractCreature owner, int amount) {
        this.amount = amount;
        this.owner = owner;
    }

    public void update() {
        AbstractPower powerToGet = owner.getPower(AdHocStrategyPower.ID);
        if (powerToGet != null
                && owner instanceof AbstractPlayer
                && ((AbstractPlayer)owner).hand.size() <= 1
                && !((AdHocStrategyPower)powerToGet).inEndTurnPeriod // 未处于回合结束
        )
        {
            powerToGet.flash();

            // 抽牌
            addToTop(new DrawCardAction(owner, amount));
            addToTop(new ReducePowerAction(owner, owner, AdHocStrategyPower.ID, amount));

        }
        this.isDone = true;
    }

}
