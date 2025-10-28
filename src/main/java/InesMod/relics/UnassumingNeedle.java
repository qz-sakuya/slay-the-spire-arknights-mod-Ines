package InesMod.relics;

import InesMod.cards.skill.PlanOfAction;
import InesMod.powers.StealsPower;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import InesMod.helpers.ModHelper;

/**
 * 中文名：无锋绣针
 */
public class UnassumingNeedle extends CustomRelic {
    public static final String ID = ModHelper.nameToId(UnassumingNeedle.class.getSimpleName());
    private static final String IMG_PATH = "InesModResources/img/relics/UnassumingNeedle.png";
    // 遗物未解锁时的轮廓。可以不使用。如果要使用，取消注释 // TODO
    // private static final String OUTLINE_PATH = "ExampleModResources/img/relics/UnassumingNeedle_Outline.png";
    private static final RelicTier RELIC_TIER = RelicTier.STARTER; // 遗物类型
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT; // 点击音效

    public UnassumingNeedle() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        // 如果你需要轮廓图，取消注释下面一行并注释上面一行，不需要就删除
        // super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 遗物初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StealsPower(AbstractDungeon.player, 3), 3));
    }

    public AbstractRelic makeCopy() {
        return new UnassumingNeedle();
    }
}