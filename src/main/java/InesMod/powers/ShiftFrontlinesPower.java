package InesMod.powers;

import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 中文名：转移阵线
 */
public class ShiftFrontlinesPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(ShiftFrontlinesPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public ShiftFrontlinesPower(AbstractCreature owner, int amount, int secondAmount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount,
                secondAmount);
    }


    @Override
    public void updateDescription() {
        if (this.amount <= 1){
            this.description = String.format(descriptions[0], this.amount)
                    + String.format(descriptions[1], this.secondAmount);
        }
        else {
            this.description = String.format(descriptions[0], this.amount)
                    + String.format(descriptions[2], this.secondAmount);; // 复数
        }
    }

    @Override
    public void onManualDiscard(AbstractCard c) {
        this.secondAmount += 1;
        this.updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new ReducePowerAction(this.owner, this.owner, ShiftFrontlinesPower.ID, this.amount));

        int count = this.amount * this.secondAmount;
        addToBot(new ApplyPowerAction(owner, owner, new InterPower(owner, count), count));
    }
}
