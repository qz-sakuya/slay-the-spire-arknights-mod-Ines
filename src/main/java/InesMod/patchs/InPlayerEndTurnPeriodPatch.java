package InesMod.patchs;


import InesMod.action.AutoUseAction;
import InesMod.cards.AbstractInesCard;
import InesMod.helpers.LogHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 监测玩家是否处于回合结束状态
 */
public class InPlayerEndTurnPeriodPatch {
    public static boolean inPlayerEndTurnPeriod = false;
//    public static ArrayList<AbstractGameAction> delayAutoUseActionList = new ArrayList<AbstractGameAction>();

    @SpirePatch(clz = AbstractCreature.class,method = "applyEndOfTurnTriggers")
    public static class EndOfTurn{
        @SpirePostfixPatch
        public static void Postfix(AbstractCreature _inst){
            if (_inst == AbstractDungeon.player) {
                inPlayerEndTurnPeriod = true;

                AutoUsePatch.setBlocking(true);

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

                AutoUsePatch.setBlocking(false);

                LogHelper.info("===InPlayerEndTurnPeriodPatch：StartOfTurn：回合结束状态={}===",inPlayerEndTurnPeriod);


//                addDelayAutoUseAction();
            }
        }
    }





//    private static void addDelayAutoUseAction(){
//        // 处理延迟的自动打出
//        Collections.reverse(delayAutoUseActionList);
//        for (AbstractGameAction action : delayAutoUseActionList) {
//            AbstractDungeon.actionManager.addToTop(action);
//
//            if (action instanceof AutoUseAction){
//                LogHelper.info("===InPlayerEndTurnPeriodPatch：addDelayAutoUseAction：重新添加：card={}===",((AutoUseAction)action).card.cardID);
//            }
//
//        }
//        delayAutoUseActionList.clear();
//    }
}
