package InesMod.powers;

import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：虚影
 * 一个很有趣的事情是，虚影因为回合最后添加，一般在“缓冲”后面，所以优先消耗缓冲，这也是强度的一个设定
 */
public class IllusionPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(IllusionPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public IllusionPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
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
