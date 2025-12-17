package InesMod.action;

import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

/**
 * 选择手牌移入抽牌堆，丢弃其他牌 的动作
 */
public class MoveCardsToDeckAndDiscardOthersAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(PathHelper.nameToId("MoveCardsToDeckAction"));
    int amount;
    boolean anyNumber;
    boolean canPickZero;

    public MoveCardsToDeckAndDiscardOthersAction(AbstractCreature target, AbstractCreature source, int amount, boolean anyNumber, boolean canPickZero) {
        this.setValues(target, source, amount);
        this.amount = amount;
        this.anyNumber = anyNumber;
        this.canPickZero = canPickZero;

        this.actionType = ActionType.CARD_MANIPULATION;
    }


    @Override
    public void update() {
        LogHelper.info("===MoveCardsToDeckAndDiscardOthersAction：start===");

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

            // 丢弃其他所有手牌
            addToTop(new DiscardAction(source, target,99,false));
        }



        tickDuration();
    }


}