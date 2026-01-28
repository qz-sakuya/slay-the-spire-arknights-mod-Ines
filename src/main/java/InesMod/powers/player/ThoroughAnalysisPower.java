package InesMod.powers.player;

import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：分析透彻
 * 此 power 的效果由 ApplyStealsToTargetAction 代行
 */
public class ThoroughAnalysisPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(ThoroughAnalysisPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public ThoroughAnalysisPower(AbstractCreature owner, int amount, int secondAmount) {
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
        this.description = String.format(descriptions[0], this.secondAmount, this.amount);
    }
}
