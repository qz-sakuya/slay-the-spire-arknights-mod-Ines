package InesMod.powers.player;

import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

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

        LogHelper.info("===ShiftFrontlinesPower: onManualDiscard后, secondAmount:{}===",this.secondAmount);
    }

    @Override
    public void atStartOfTurn() {

        LogHelper.info("===ShiftFrontlinesPower: atStartOfTurn, this.amount={}，this.secondAmount={}===",this.amount,this.secondAmount);

        flash();
        addToBot(new ReducePowerAction(this.owner, this.owner, ShiftFrontlinesPower.ID, this.amount));

        int count = this.amount * this.secondAmount;
        if (count > 0){
            addToBot(new ApplyPowerAction(owner, owner, new InterPower(owner, count), count));

            LogHelper.info("===ShiftFrontlinesPower: 获得情报，层数：{}===",count);
        }
    }
}
