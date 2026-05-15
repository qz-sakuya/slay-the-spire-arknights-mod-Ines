package InesMod.relics;

import InesMod.action.RoastPotatoAction;
import InesMod.helpers.PathHelper;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

/**
 * 中文名：独眼面罩
 */
public class OneEyeMask extends AbstractInesRelic {
    public static final String ID = PathHelper.nameToId(OneEyeMask.class.getSimpleName());

    public OneEyeMask(){
        super(ID, true, false, RelicTier.UNCOMMON, LandingSound.FLAT);

        this.counter = 0;
    }

    // 遗物初始描述
    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();

        int strengthToApply = this.counter / 7;
        strengthToApply =  MathUtils.floor(strengthToApply);
        if (strengthToApply > 0) {
            flash();
            addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, strengthToApply), strengthToApply));
        }
    }

    @Override
    public void onVictory() {
        flash();
        this.counter += 1;
    }



    @Override
    public boolean canSpawn() {
        // 仅前三层出现
        return AbstractDungeon.actNum <= 3;
    }
}