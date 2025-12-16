package InesMod.powers;

import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

/**
 * 中文名：影之疆土
 * 此 power 的效果由 InvisibilityPower 代行
 */
public class ShadowTerritoryPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(ShadowTerritoryPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public ShadowTerritoryPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
    }


    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.amount);
    }
}
