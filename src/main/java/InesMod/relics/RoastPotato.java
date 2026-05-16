package InesMod.relics;

import InesMod.action.RoastPotatoAction;
import InesMod.action.UpgradeRelicCounterAction;
import InesMod.helpers.PathHelper;
import InesMod.powers.player.StealsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * 中文名：烤土豆
 */
public class RoastPotato extends AbstractInesRelic {
    public static final String ID = PathHelper.nameToId(RoastPotato.class.getSimpleName());

    public RoastPotato(){
        super(ID, false, false, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    // 遗物初始描述
    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }



    @Override
    public void onPlayerEndTurn() {
        addToBot(new RoastPotatoAction(this));
    }


}