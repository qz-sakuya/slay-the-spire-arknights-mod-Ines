package InesMod.relics;

import InesMod.action.UpgradeRelicCounterAction;
import InesMod.powers.player.StealsPower;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import InesMod.helpers.PathHelper;

/**
 * 中文名：无锋绣针
 */
public class UnassumingNeedle extends AbstractInesRelic {
    public static final String ID = PathHelper.nameToId(UnassumingNeedle.class.getSimpleName());

    public UnassumingNeedle(){
        super(ID, false, false, RelicTier.STARTER, LandingSound.FLAT);
    }

    // 遗物初始描述
    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();

        this.counter = 3;
    }

    @Override
    public void atTurnStart() {
        if (this.counter > 0) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StealsPower(AbstractDungeon.player, this.counter), this.counter));
            addToBot(new UpgradeRelicCounterAction(this,-1));
        }
    }


}