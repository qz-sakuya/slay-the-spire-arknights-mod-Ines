package InesMod.action;

import InesMod.helpers.LogHelper;
import InesMod.vfx.InesAttackEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

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
        LogHelper.info("===Ines:CloseQuartersCombatAction：Start===");
        if(source.currentBlock == 0){
            this.addToTop(new DamageAction(target, new DamageInfo(source, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            this.addToTop(new InesAttackEffect(source,1));
        }
        this.addToTop(new DamageAction(target, new DamageInfo(source, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));


        this.isDone = true;
    }
}
