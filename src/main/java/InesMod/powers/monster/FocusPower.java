package InesMod.powers.monster;

import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

/**
 * 中文名：专注
 * 英文名：Focus
 * 敌方power
 * 图标
 */
public class FocusPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(FocusPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public FocusPower(AbstractCreature owner, int amount, boolean spawnElite) {
        super(ID,
                true,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
    }

    @Override // 成功造成伤害时
    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && info.type != DamageInfo.DamageType.THORNS) {
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount), amount));
        } else {
            AbstractPower strengthPower = owner.getPower(StrengthPower.POWER_ID);
            if (strengthPower != null && strengthPower.amount > 0) {
                int amountToLoss = strengthPower.amount;
                if (amountToLoss > 2) {
                    amountToLoss = 2;
                }
                addToBot(new ReducePowerAction(owner, owner, StrengthPower.POWER_ID, amountToLoss));
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0]);
    }
}

