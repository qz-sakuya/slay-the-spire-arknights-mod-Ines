package InesMod.relics;

import InesMod.action.ReduceAndKeepPowerAction;
import InesMod.action.UpgradeRelicCounterAction;
import InesMod.helpers.PathHelper;
import InesMod.powers.player.StealsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * 中文名：虚实绊线
 * 敏捷效果由偷取相关power代行
 */
public class FeintTripwire extends AbstractInesRelic {
    public static final String ID = PathHelper.nameToId(FeintTripwire.class.getSimpleName());

    public FeintTripwire(){
        super(ID, false, false, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    // 遗物初始描述
    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onPlayerEndTurn() {
        addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, StealsPower.ID, 1));
    }
}