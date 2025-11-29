package InesMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * 将牌加入手牌 的 action
 */
public class AddCardToHandAction extends AbstractGameAction {
    AbstractCard card;

    public AddCardToHandAction(AbstractCard card) {
        this.card = card;
    }

    public void update() {
        AbstractDungeon.player.hand.addToHand(card);
        this.isDone = true;
    }
}