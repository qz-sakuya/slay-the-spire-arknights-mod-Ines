package InesMod.action;

import InesMod.helpers.PathHelper;
import InesMod.helpers.TextHelper;
import InesMod.modcore.InesModMain;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

/**
 * 选择手牌移入抽牌堆 的动作
 */
public class MoveCardsToDeckAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(PathHelper.nameToId("MoveCardsToDeckAction"));
    int amount;
    boolean anyNumber;
    boolean canPickZero;

    public MoveCardsToDeckAction(AbstractCreature target, AbstractCreature source,int amount, boolean anyNumber, boolean canPickZero) {
        this.setValues(target, source, amount);
        this.amount = amount;
        this.anyNumber = anyNumber;
        this.canPickZero = canPickZero;

        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    }


    @Override
    public void update() {
        InesModMain.logger.info("===MoveCardsToDeckAction：start===");

        if (this.duration == 0.5F) {
            AbstractDungeon.handCardSelectScreen.open(uiStrings.TEXT[0], amount, anyNumber, canPickZero);
            addToBot(new WaitAction(0.25F));
            tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                // 移入抽牌堆
                AbstractDungeon.player.hand.moveToDeck(c, true);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }

        tickDuration();
    }


}