package InesMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;

/**
 * 常规自动打出
 */
public class UpgradeRelicCounterAction extends AbstractGameAction {
    private AbstractRelic relic;
    int mnt;

    public UpgradeRelicCounterAction(AbstractRelic relic, int mnt) {
        this.relic = relic;
        this.mnt = mnt;
    }

    public void update() {
        this.relic.counter += mnt;

        this.isDone = true;
    }
}
