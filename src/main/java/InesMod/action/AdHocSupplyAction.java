package InesMod.action;

import InesMod.powers.AdHocSupplyPower;
import InesMod.powers.AgentVanguardPower;
import InesMod.powers.InterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
        if (owner instanceof AbstractPlayer && ((AbstractPlayer)owner).hand.size() <= 1){
            // 获得能量
            addToTop(new GainEnergyAction(1));
            addToTop(new ReducePowerAction(owner, owner, AdHocSupplyPower.ID, amount));

            AbstractPower powerToGet = owner.getPower(AdHocSupplyPower.ID);
            if (powerToGet != null) {
                powerToGet.flash();
            }
        }
        this.isDone = true;
    }
}
