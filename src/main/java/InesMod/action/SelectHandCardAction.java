package InesMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 选择手牌（带筛选器） 的动作
 */
public class SelectHandCardAction extends AbstractGameAction {

    private Consumer<ArrayList<AbstractCard>> actionConsumer;
    private Predicate<AbstractCard> cardFilter;
    private ArrayList<AbstractCard> cannotDuplicate = new ArrayList<>();

    private String name = "";
    private boolean anyNumber = false;
    private boolean canPickZero = false;

    public SelectHandCardAction(String name, int amount, Predicate<AbstractCard> cardFilter, Consumer<ArrayList<AbstractCard>> actionConsumer, boolean anyNumber, boolean canPickZero) {
        this.actionConsumer = actionConsumer;
        this.cardFilter = cardFilter;

        this.name = name;
        this.anyNumber = anyNumber;
        this.canPickZero = canPickZero;

        this.actionType = ActionType.CARD_MANIPULATION;
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public SelectHandCardAction(String name, int amount, Predicate<AbstractCard> cardFilter, Consumer<ArrayList<AbstractCard>> actionConsumer) {
        this(name, amount, cardFilter, actionConsumer, false, false);
    }

    public SelectHandCardAction(String name, int amount, Consumer<ArrayList<AbstractCard>> actionConsumer, boolean anyNumber, boolean canPickZero) {
        this(name, amount, null, actionConsumer, anyNumber, canPickZero);
    }

    public SelectHandCardAction(String name, int amount, Consumer<ArrayList<AbstractCard>> actionConsumer) {
        this(name, amount, null, actionConsumer);
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {        //与上面的构造体里面的  this.duration = Settings.ACTION_DUR_FAST对应 仅在action的第一帧进行卡牌的设置
            if (AbstractDungeon.player.hand.isEmpty()) {  // 如果手牌为空，结束action
                isDone = true;
                return;
            }

            if (this.cardFilter != null) {  //根据 cardFilter 筛选出不能选的卡牌
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (!cardFilter.test(c)) {
                        cannotDuplicate.add(c);
                    }
                }
            }

            if (cannotDuplicate.size() >= AbstractDungeon.player.hand.group.size()) {  // 如果不能选的卡牌跟手牌一样大，也就是没有可选的牌，结束action
                isDone = true;
                return;
            }

            AbstractDungeon.player.hand.group.removeAll(cannotDuplicate);  // 临时移除不可选的卡牌 ，剩下的都是可选的


            // 在要选的手牌大于等于可选手牌 且不能自由选牌也不能不选时  时进行快速选择，
            if (AbstractDungeon.player.hand.group.size() <= this.amount && !this.anyNumber && !this.canPickZero) {
                actionConsumer.accept(new ArrayList<>(AbstractDungeon.player.hand.group));
                // 对可选卡牌进行操作，如果不是消耗之类的，需要你手动将这些卡牌返回手牌，类似于下面的returnCards 。 复制一个新的列表是为了防止你在遍历可选手牌的时候把它移动到弃牌堆导致数组长度变化错误
                returnCards(); // 将不能被选卡牌返回手牌

                isDone = true;
                return;
            }

            //打开选牌窗口
            AbstractDungeon.handCardSelectScreen.open(this.name, amount, this.anyNumber, this.canPickZero);
            tickDuration();  // 进行一帧 duration 计算 并结束这次 update
            return;
        }

        // 选牌窗口打开时这个action将被打开的选牌窗口暂停更新，当其结束时被选卡牌将存在 AbstractDungeon.handCardSelectScreen.selectedCards 里面 同时用
        // AbstractDungeon.handCardSelectScreen.wereCardsRetrieved 来判断被选卡牌是否进行过处理，也即是被选卡牌尚未返回手牌。
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                actionConsumer.accept(AbstractDungeon.handCardSelectScreen.selectedCards.group);  // 同上 执行被选卡牌要做的操作 ，因为已经在别的的列表里面，所以不用复制一个新的列表
                CardCrawlGame.dungeon.checkForPactAchievement(); // 如果这个action是消耗手牌一类，有成就需要检测
            }

            returnCards(); // 将不能被选卡牌返回手牌
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true; // 手牌已经返回
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear(); // 清空临时存储的 被选卡牌列表
            this.isDone = true;
        }

        tickDuration(); // 给选完卡牌之后卡牌进行操作还有返回手牌动画的时间
    }

    private void returnCards() {
        for (AbstractCard c : this.cannotDuplicate) { // 将不可选卡牌返回手牌
            AbstractDungeon.player.hand.addToTop(c);
        }

        AbstractDungeon.player.hand.refreshHandLayout(); //刷新手牌排版
    }
}


