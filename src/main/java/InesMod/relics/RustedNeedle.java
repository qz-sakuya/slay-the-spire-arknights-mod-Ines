package InesMod.relics;

import InesMod.helpers.PathHelper;
import InesMod.powers.player.StealsPower;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

/**
 * 中文名：破败绣针
 */
public class RustedNeedle extends CustomRelic {
    public static final String ID = PathHelper.nameToId(RustedNeedle.class.getSimpleName());
    private static final String IMG_PATH = "InesModResources/img/relics/RustedNeedle.png";
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public RustedNeedle() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 遗物初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StealsPower(AbstractDungeon.player, 2), 2));
    }

    @Override
    public void obtain() {
        updateDescription(AbstractDungeon.player.chosenClass);
        if (AbstractDungeon.player.hasRelic(PathHelper.nameToId(UnassumingNeedle.class.getSimpleName()))) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(PathHelper.nameToId(UnassumingNeedle.class.getSimpleName()))) {
                    // 将本遗物与位置i替换
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            this.counter = -1;
            super.obtain();
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(PathHelper.nameToId(UnassumingNeedle.class.getSimpleName()));
    }

    public AbstractRelic makeCopy() {
        return new RustedNeedle();
    }
}