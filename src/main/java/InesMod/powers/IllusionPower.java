package InesMod.powers;

import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：虚影
 */
public class IllusionPower extends AbstractInesPower {
    public static final String ID = ModHelper.nameToId(IllusionPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public IllusionPower(AbstractCreature owner, int amount) {
        super(ID,
                true,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount); // 此能力不可叠加
    }


    @Override
    public void updateDescription() {
        this.description = descriptions[0];
    }


    @Override
    public void atStartOfTurn() {
        addToBot(new GainEnergyAction(1));
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, IllusionPower.ID));
    }




    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, IllusionPower.ID));
        }
        return 0; // 使伤害归零
    }
}
