package InesMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

/**
 * 延迟一次 action 的 action
 */
public class DelayToAddAction extends AbstractGameAction {
    private final AbstractGameAction action;

    public DelayToAddAction(AbstractGameAction action) {
        this.action = action;
    }

    public void update() {
        addToBot(action);

        this.isDone = true;
    }
}
