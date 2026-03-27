package InesMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * 强制延迟 的 action
 * 在 WaitAction 的基础上，去掉快速模式的限制
 */
public class ForceWaitAction extends AbstractGameAction {
    public ForceWaitAction(float setDur) {
        this.setValues(null, null, 0);
        this.duration = setDur;

        this.actionType = ActionType.WAIT;
    }

    public void update() {
        this.tickDuration();
    }
}
