package InesMod.action;

import InesMod.helpers.LogHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * 自动打出，失败消耗
 */
public class AutoUseOrExhaustAction extends AbstractGameAction {
    private AbstractCard card;

    public AutoUseOrExhaustAction(AbstractCard card) {
        this.card = card;
    }

    public void update() {
        LogHelper.info("===AutoUseOrExhaustAction：开始，card={}===",card.cardID);
        if (!this.card.hasEnoughEnergy() || !this.card.cardPlayable(null)){
            // 无法打出则消耗
//            addToTop(new ExhaustSpecificCardAction(this.card, AbstractDungeon.player.hand));

            LogHelper.info("===AutoUseOrExhaustAction：无法打出，消耗===");
        }
        else {
            // 立即消耗能量打出
            this.card.applyPowers();
            LogHelper.info("===AutoUseOrExhaustAction：自动打出：===",this.card.name);
            addToTop(new NewQueueCardAction(this.card, true, true, true));
            addToTop(new LoseEnergyAction(this.card.cost));
        }
        this.isDone = true;
    }
}
