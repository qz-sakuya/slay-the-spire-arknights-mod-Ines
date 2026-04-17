package InesMod.action;

import InesMod.helpers.LogHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * 常规自动打出
 */
public class AutoUseAction extends AbstractGameAction {
    private AbstractCard card;

    public AutoUseAction(AbstractCard card) {
        this.card = card;
    }

    public void update() {
        if (!this.card.hasEnoughEnergy() || !this.card.cardPlayable(null)){
            this.isDone = true;
            return;
        }
        // 立即消耗能量打出
        this.card.applyPowers();
        LogHelper.info("===AutoUseAction：自动打出：===",this.card.name);
        addToTop(new NewQueueCardAction(this.card, true, true, true));
        addToTop(new LoseEnergyAction(this.card.cost));
        this.isDone = true;
    }
}
