package InesMod.powers;

import InesMod.helpers.ModHelper;
import InesMod.modcore.InesModMain;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

/**
 * 中文名：偷取力量
 */
public class StrengthStolenPower extends AbstractInesPower {
    public static final String ID = ModHelper.nameToId(StrengthStolenPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源


    public StrengthStolenPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount > 999) {
            this.amount = 999;
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.amount, this.amount);
    }


    public void atEndOfTurn(boolean isPlayer) {
        flash();
        addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount), this.amount));
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, StrengthStolenPower.ID));
    }
}
