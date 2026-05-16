package InesMod.action;

import InesMod.cards.AbstractInesCard;
import InesMod.helpers.LogHelper;
import InesMod.patchs.AutoUseManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * 自动打出
 */
public class AutoUseAction extends AbstractGameAction {
    public final AbstractCard card;


    public AutoUseAction(AbstractCard card) {
        this.card = card;
    }

    public void update() {
        if (card == null
            || !AbstractDungeon.player.hand.group.contains(card)) {
            this.isDone = true;
            return;
        }


        LogHelper.info("===AutoUseAction：开始，card={}===",card.cardID);



        if (!this.card.hasEnoughEnergy() || !this.card.cardPlayable(null)){
            LogHelper.info("===AutoUseAction：无法打出，card={}===",card.cardID);

            AutoUseManager.setBlocking(false);

            if (this.card instanceof AbstractInesCard) {
                ((AbstractInesCard)card).triggerOnAutoUseFail();
            }
        }
        else {
            // 立即消耗能量打出
            this.card.applyPowers();
            LogHelper.info("===AutoUseAction：自动打出，card={}===",card.cardID);
            addToTop(new NewQueueCardAction(this.card, true, true, true));
            if (!this.card.freeToPlay()) {
                addToTop(new LoseEnergyAction(this.card.cost));
            }


        }

        this.isDone = true;
    }




}
