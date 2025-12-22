package InesMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

/**
 * 情绪干扰 的效果
 */
public class EmotionalDisturbanceAction extends AbstractGameAction {
    AbstractPlayer p;
    AbstractMonster m;

    public EmotionalDisturbanceAction(AbstractPlayer p, AbstractMonster m, int amount) {
        this.p = p;
        this.m = m;
        this.amount = amount;
    }

    public void update() {
        if (this.m != null && this.m.getIntentBaseDmg() < 0) {
            addToTop(new ApplyPowerAction(m, p, new WeakPower(this.m, amount, false), amount));
            addToTop(new ApplyPowerAction(m, p, new VulnerablePower(this.m, amount, false), amount));
        }


        this.isDone = true;
    }
}
