package InesMod.powers;

import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：佣兵手段
 * 此 power 的效果由 StealsPower 、 InsightPower 和 ApplyStealsToTargetAction 代行
 */
public class MercenaryTacticsPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(MercenaryTacticsPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public MercenaryTacticsPower(AbstractCreature owner, int amount) {
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
