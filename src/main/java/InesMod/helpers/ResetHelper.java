package InesMod.helpers;

import InesMod.characters.Ines;
import InesMod.patchs.AutoUseManager;
import InesMod.patchs.ExhaustCountInCombatManager;
import InesMod.vfx.DefenseArtilleryMeterUponManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * 统一重置所有特殊变量、计数器、特效等
 */
public class ResetHelper {

    public static void resetOnce() {
        LogHelper.info("===ResetHelper：重置===");

        // 清空消耗计数器
        ExhaustCountInCombatManager.set(0);

        // 清空玩家计数器
        if (AbstractDungeon.player instanceof Ines){
            ((Ines)AbstractDungeon.player).resetAllCustomCounter();
        }

        // 清空自动打出列表
        AutoUseManager.clearTask();

        // 清空城防炮特效
        DefenseArtilleryMeterUponManager.clearEffect();
    }
}
