package InesMod.action;

import InesMod.enums.InesCardTags;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.List;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.handCardSelectScreen;

/**
 * 重点信息 的效果
 */
public class CriticalInfoAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(PathHelper.nameToId("RetainCardsThisTurnAction"));
    public static final String[] TEXT = uiStrings.TEXT;



    private AbstractPlayer p;
    private static final float DURATION = Settings.ACTION_DUR_XFAST;

    public CriticalInfoAction(AbstractCreature target, AbstractCreature source, int amount) {
        setValues(target, source, amount);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = DURATION;
        this.p = (AbstractPlayer)target;
    }

    public void update() {
        if (this.duration == DURATION) {
            if (this.p.hand.isEmpty()) {
                this.isDone = true;
                return;
            }

            handCardSelectScreen.open(TEXT[0], amount, false, true, false, false, true);

                tickDuration();
            return;
        }

        if (!handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : handCardSelectScreen.selectedCards.group) {
                if (!c.selfRetain && !c.tags.contains(InesCardTags.RetainThisTurn)) {
                    // 使选中的牌在本回合保留
                    c.retain = true;

                    // 添加tag（文本由patch处理）
                    c.tags.add(InesCardTags.RetainThisTurn);
                    c.initializeDescription();
                }
                addToTop(new AddCardToHandAction(c));
            }

            handCardSelectScreen.selectedCards.clear();
            handCardSelectScreen.wereCardsRetrieved = true;


            // 丢弃其他所有手牌
            addToTop(new DiscardAction(source, target,99,false));


        }

        tickDuration();
    }
}