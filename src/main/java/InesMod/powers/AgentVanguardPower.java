package InesMod.powers;

import InesMod.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：情报官
 * 此power的效果由StealsPower代行
 */
public class AgentVanguardPower extends AbstractInesPower {
    public static final String ID = ModHelper.nameToId(AgentVanguardPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public AgentVanguardPower(AbstractCreature owner, int amount) {
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
}
