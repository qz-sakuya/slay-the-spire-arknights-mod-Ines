package InesMod.powers.player;

import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：不能获得隐匿
 * 此 power 的效果由 ApplyPowerActionPatch 代行
 */
public class NoInvisibilityPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(NoInvisibilityPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public NoInvisibilityPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.DEBUFF,
                amount);
        this.isTurnBased = true;
    }


    @Override
    public void updateDescription() {
        if (this.amount <= 1){
            this.description = String.format(descriptions[0], this.amount);
        }
        else {
            this.description = String.format(descriptions[1], this.amount); // 复数
        }
    }


    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount == 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, NoInvisibilityPower.ID));
        } else {
            addToBot(new ReducePowerAction(this.owner, this.owner, NoInvisibilityPower.ID, 1));
        }
    }
}
