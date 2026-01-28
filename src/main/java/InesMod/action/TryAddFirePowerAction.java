package InesMod.action;

import InesMod.powers.monster.CFPPower;
import InesMod.powers.monster.FirePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 城防炮充能 判定是否添加 炮击！ 的 action
 */
public class TryAddFirePowerAction extends AbstractGameAction {
    private int addAmount;
    public TryAddFirePowerAction(AbstractCreature target, AbstractCreature source, int addAmount) {
        this.target = target;
        this.source = source;
        this.addAmount = addAmount;
    }

    public void update() {
        AbstractPower powerToGet = target.getPower(CFPPower.ID);
        if (powerToGet instanceof CFPPower) {
            if (powerToGet.amount == ((CFPPower) powerToGet).secondAmount) {
                addToBot(new ApplyNonStackPowerAction(target, source, new FirePower(target, addAmount)));
            }
        }


        this.isDone = true;
    }
}
