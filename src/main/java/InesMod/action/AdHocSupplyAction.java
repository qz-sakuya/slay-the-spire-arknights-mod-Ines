package InesMod.action;

import InesMod.powers.player.AdHocSupplyPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 临时补给 的效果
 */
public class AdHocSupplyAction extends AbstractGameAction {
    private final AbstractCreature owner;
    private final int amount;

    public AdHocSupplyAction(AbstractCreature owner, int amount) {
        this.amount = amount;
        this.owner = owner;
    }

    public void update() {
        AbstractPower powerToGet = owner.getPower(AdHocSupplyPower.ID);

        if (powerToGet != null
                && owner instanceof AbstractPlayer
                && ((AbstractPlayer)owner).hand.size() <= 1
                && !((AdHocSupplyPower)powerToGet).inEndTurnPeriod // 未处于回合结束
        )
        {
            powerToGet.flash();

            // 获得能量
            addToTop(new GainEnergyAction(amount));
            addToTop(new ReducePowerAction(owner, owner, AdHocSupplyPower.ID, amount));
        }
        this.isDone = true;
    }
}
