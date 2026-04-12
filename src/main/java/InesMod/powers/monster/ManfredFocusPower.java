package InesMod.powers.monster;

import InesMod.action.ManfredFocusAction;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：专注
 * 英文名：Focus
 * 敌方power
 * 图标
 */
public class ManfredFocusPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(ManfredFocusPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public int strengthAmtToChange = 0;

    public ManfredFocusPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount
                );
    }


    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        strengthAmtToChange = - amount;

        addToBot(new ManfredFocusAction(owner));

        return damageAmount;
    }

    @Override // 成功对玩家造成伤害时
    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        strengthAmtToChange = amount;

//        if (damageAmount > 0 && info.type != DamageInfo.DamageType.THORNS) {
//            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, this.amount), this.amount));
//        } else {
//            AbstractPower strengthPower = owner.getPower(StrengthPower.POWER_ID);
//            if (strengthPower != null && strengthPower.amount > 0) {
//                int amountToLoss = this.amount;
//                if (amountToLoss > strengthPower.amount) {
//                    amountToLoss = strengthPower.amount;
//                }
//
//                LogHelper.info("===FocusPower：onInflictDamage：减少力量：{}===", amountToLoss);
//                addToBot(new ReducePowerAction(owner, owner, StrengthPower.POWER_ID, amountToLoss));
//            }
//        }
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], amount, amount);
    }
}

