package InesMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 表格选卡，包括筛选器
 * 不建议用于手牌（因为有另一个实现）
 * 不对待选牌进行排序，如有需要，请提前处理
 */
public class SelectPileCardAction extends AbstractGameAction {
    // 借用原版文本
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("HandCardSelectScreen").TEXT;

    private final int numberOfCards;
    private final boolean optional; // 如果 optional 可以选任意张，包括0。否则强制选 numberOfCards 张

    private final Consumer<ArrayList<AbstractCard>> actionConsumer;
    private final Predicate<AbstractCard> cardFilter;

    private final ArrayList<AbstractCard> cardList;
    private final String name;

    boolean sortBeforeSelect;


    public SelectPileCardAction(ArrayList<AbstractCard> cardList, String name, int numberOfCards, Predicate<AbstractCard> cardFilter, Consumer<ArrayList<AbstractCard>> actionConsumer, boolean optional, boolean sortBeforeSelect) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.cardList = cardList;
        this.numberOfCards = numberOfCards;
        this.optional = optional;
        this.name = name;
        this.sortBeforeSelect = sortBeforeSelect;

        this.actionConsumer = actionConsumer;
        this.cardFilter = cardFilter;


    }

    public void update() {
        if (this.duration == this.startDuration) {
            // 先筛选卡
            ArrayList<AbstractCard> filteredCards = new ArrayList<>();
            for (AbstractCard c : cardList) {
                if (this.cardFilter.test(c)) {
                    filteredCards.add(c);
                }
            }

            if (filteredCards.isEmpty() || this.numberOfCards <= 0) {
                this.isDone = true;
                return;
            }

            if (filteredCards.size() <= this.numberOfCards && !this.optional) {
                // 执行被选卡牌要做的操作
                actionConsumer.accept(filteredCards);

                this.isDone = true;
                return;
            }

            CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : filteredCards) {
                temp.addToTop(c);
            }

            // 对选牌表格的牌进行排序
            if (this.sortBeforeSelect) {
                temp.sortAlphabetically(true);
                temp.sortByRarityPlusStatusCardType(false);
            }

            if (this.numberOfCards == 1) {
                if (this.optional) {
                    AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, TEXT[5] + name);
                } else {
                    AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, TEXT[5] + name, false);
                }
            } else if (this.optional) {
                AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, TEXT[2] + this.numberOfCards + TEXT[3] + name);
            } else {
                AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, TEXT[2] + this.numberOfCards + TEXT[3] + name, false);
            }

            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            // 执行被选卡牌要做的操作
            actionConsumer.accept(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        tickDuration();
    }
}