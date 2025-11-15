package InesMod.action;

import InesMod.powers.AdHocStrategyPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 近身格斗 的效果
 */
public class CloseQuartersCombatAction extends AbstractGameAction {
    private final int damage;
    private final int magicNumber;

    public CloseQuartersCombatAction(AbstractCreature source, AbstractCreature target,  int damage, int magicNumber) {
        this.damage = damage;
        this.magicNumber = magicNumber;
        this.source = source;
        this.target = target;
    }

    public void update() {
        int tempDamage = this.damage;
        if(source.currentBlock == 0){
            tempDamage += this.magicNumber;

            // 重攻击动画
            this.addToTop(new DamageAction(target, new DamageInfo(source, tempDamage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
        else{
            // 轻攻击动画
            this.addToTop(new DamageAction(target, new DamageInfo(source, tempDamage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }

        this.isDone = true;
    }
}
