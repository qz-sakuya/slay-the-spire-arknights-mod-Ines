package InesMod.action;

import InesMod.helpers.LogHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 从抽牌堆和弃牌堆随机检索n张牌
 */
public class RandomSearchCardAction extends AbstractGameAction {
    // 借用原版文本
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("HandCardSelectScreen").TEXT;

    private final int numberOfCards;

    private final Consumer<ArrayList<AbstractCard>> actionConsumer;
    private final Predicate<AbstractCard> cardFilter;



    public RandomSearchCardAction(int numberOfCards, Predicate<AbstractCard> cardFilter, Consumer<ArrayList<AbstractCard>> actionConsumer) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;

        this.numberOfCards = numberOfCards;

        this.actionConsumer = actionConsumer;
        this.cardFilter = cardFilter;


    }

    public void update() {
        // 先筛选卡
        ArrayList<AbstractCard> filteredCards = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (this.cardFilter.test(c)) {
                filteredCards.add(c);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (this.cardFilter.test(c)) {
                filteredCards.add(c);
            }
        }

        if (filteredCards.isEmpty() || this.numberOfCards <= 0) {
            this.isDone = true;
            return;
        }




        if (filteredCards.size() <= this.numberOfCards) {
            // 执行被选卡牌要做的操作
            actionConsumer.accept(filteredCards);
            this.isDone = true;
            return;
        }

        ArrayList<AbstractCard> randomCards = new ArrayList<>();

        // 随机选n张卡
        for (int i = 0; i < this.numberOfCards; i++) {
            int size = filteredCards.size();
            int randomIndex = AbstractDungeon.cardRandomRng.random(0, size - 1);
            AbstractCard picked = filteredCards.remove(randomIndex); // 移除并获取，确保不重复
            randomCards.add(picked);
        }

        // 执行被选卡牌要做的操作
        actionConsumer.accept(randomCards);

        this.isDone = true;
    }
}