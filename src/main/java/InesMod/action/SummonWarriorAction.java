package InesMod.action;

import InesMod.monsters.Chapter10.SarkazHeirbearerWarrior;
import InesMod.powers.AbstractInesPower;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.SummonGremlinAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.GremlinLeader;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 曼弗雷德 召唤 战士
 */
public class SummonWarriorAction extends AbstractGameAction {

    private AbstractMonster m;

    float x;
    float y;
    int slot;

    public SummonWarriorAction(int slot, float x, float y) {
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_FAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_LONG;
        }
        this.duration = this.startDuration;

        this.slot = slot;
        this.x = x;
        this.y = y;


        this.m = new SarkazHeirbearerWarrior(x,y);

        // 播放走路动画
        this.m.state.setAnimation(0, "Move", true);


        for (AbstractRelic r : AbstractDungeon.player.relics) {
            r.onSpawnMonster(this.m);
        }

        // 触发自定义回调
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof AbstractInesPower) {
                ((AbstractInesPower) power).onSpawnMonster(this.m);
            }
        }

        for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
            for (AbstractPower power : mon.powers) {
                if (power instanceof AbstractInesPower) {
                    ((AbstractInesPower) power).onSpawnMonster(this.m);
                }
            }
        }

    }



    public void update() {
        if (this.duration == this.startDuration) {
            this.m.animX = 1200.0F * Settings.xScale;
            this.m.init();
            this.m.applyPowers();
            (AbstractDungeon.getCurrRoom()).monsters.addMonster(this.slot, this.m);

            if (ModHelper.isModEnabled("Lethality")) {
                addToBot(new ApplyPowerAction(this.m, this.m, new StrengthPower(this.m, 3), 3));
            }

            if (ModHelper.isModEnabled("Time Dilation")) {
                addToBot(new ApplyPowerAction(this.m, this.m, new SlowPower(this.m, 0)));
            }
        }

        tickDuration();

        if (this.isDone) {
            this.m.animX = 0.0F;
            this.m.showHealthBar();
            this.m.usePreBattleAction();

            // 播放待机动画
            this.m.state.setAnimation(0, "Idle", true);
        } else {
            this.m.animX = Interpolation.fade.apply(0.0F, 1200.0F * Settings.xScale, this.duration);
        }
    }
}