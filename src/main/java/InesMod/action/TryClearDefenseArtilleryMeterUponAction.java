package InesMod.action;

import InesMod.helpers.LogHelper;
import InesMod.monsters.Chapter10.Teekazwurtzen;
import InesMod.powers.monster.DefenseArtilleryMeterPower;
import InesMod.powers.monster.FirePower;
import InesMod.vfx.DefenseArtilleryMeterUponManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 城防炮充能 判定是否添加 炮击！ 的 action
 */
public class TryClearDefenseArtilleryMeterUponAction extends AbstractGameAction {

    public TryClearDefenseArtilleryMeterUponAction(AbstractCreature source) {
        this.source = source;
    }

    public void update() {
        LogHelper.info("===InesMod：TryClearDefenseArtilleryMeterUponAction：start===");
        Work(this.source);

        this.isDone = true;
    }

    public static void Work(AbstractCreature source) {
        // 如果有其它单位有 城防炮充能 或 炮击 power，跳过
        boolean stillHavePower = false;
        for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
            if (mon == source){
                continue;
            }

            AbstractPower DamPower = mon.getPower(DefenseArtilleryMeterPower.ID);
            if (DamPower instanceof DefenseArtilleryMeterPower) {
                stillHavePower = true;
            }

            AbstractPower firePower = mon.getPower(FirePower.ID);
            if (firePower instanceof FirePower) {
                stillHavePower = true;
            }
        }

        AbstractPower powerToGet = AbstractDungeon.player.getPower(FirePower.ID);
        if (powerToGet != null) {
            stillHavePower = true;
        }


        if (!stillHavePower) {
            // 删除特效
            DefenseArtilleryMeterUponManager.clearEffect();

            LogHelper.info("===InesMod：TryClearDefenseArtilleryMeterUponAction：成功===");
        }
    }
}
