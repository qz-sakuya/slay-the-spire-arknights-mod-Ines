package InesMod.patchs;


import InesMod.helpers.LogHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * 监测玩家是否处于回合结束状态
 */
public class InPlayerEndTurnPeriodManager {
    public static boolean inPlayerEndTurnPeriod = false;


    @SpirePatch(clz = AbstractCreature.class,method = "applyEndOfTurnTriggers")
    public static class EndOfTurn{
        @SpirePostfixPatch
        public static void Postfix(AbstractCreature _inst){
            if (_inst == AbstractDungeon.player) {
                inPlayerEndTurnPeriod = true;

                AutoUseManager.setBlocking(true);

                LogHelper.info("===InPlayerEndTurnPeriodPatch：EndOfTurn：回合结束状态={}===",inPlayerEndTurnPeriod);
            }
        }
    }

    @SpirePatch(clz = AbstractCreature.class,method = "applyStartOfTurnPowers")
    public static class StartOfTurn{
        @SpirePostfixPatch
        public static void Postfix(AbstractCreature _inst){
            if (_inst == AbstractDungeon.player){
                inPlayerEndTurnPeriod = false;

                AutoUseManager.setBlocking(false);

                LogHelper.info("===InPlayerEndTurnPeriodPatch：StartOfTurn：回合结束状态={}===",inPlayerEndTurnPeriod);


            }
        }
    }


}
