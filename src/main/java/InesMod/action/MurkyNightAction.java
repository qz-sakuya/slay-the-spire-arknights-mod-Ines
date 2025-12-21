package InesMod.action;

import InesMod.cards.status.ShadowWhistle;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * 暗夜无明 的效果
 */
public class MurkyNightAction extends AbstractGameAction {
    private final AbstractCard sourceCard;
    private final AbstractCard targetCard;
    private final int amount;

    public MurkyNightAction(AbstractCard sourceCard, AbstractCard targetCard, int amount) {
        this.sourceCard = sourceCard;
        this.targetCard = targetCard;
        this.amount = amount;
    }

    public void update() {
        // 潜伏牌在手牌才触发效果
        if (AbstractDungeon.player.hand.group.contains(sourceCard)) {
            addToBot(new ExhaustSpecificCardAction(targetCard, AbstractDungeon.player.discardPile));

            addToBot(new MakeTempCardInHandAction(new ShadowWhistle(), amount)); // 生成影哨
        }
        this.isDone = true;
    }
}
