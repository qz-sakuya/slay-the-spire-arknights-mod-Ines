package InesMod.patchs;


import InesMod.action.AutoUseAction;
import InesMod.cards.AbstractInesCard;
import InesMod.helpers.LogHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

/**
 * 自动打出 的管理类
 * 回合结束状态一定为阻挡，由 InPlayerEndTurnPeriodPatch 辅助处理
 * 出完牌，或自动打出 action 中失败，则解除阻挡
 */
public class AutoUsePatch {
    public static boolean isBlocking = false;
    public static ArrayList<AbstractGameAction> taskList = new ArrayList<AbstractGameAction>();


    public static void setBlocking(boolean newBlocking) {
        if (newBlocking) {
            isBlocking = true;

            LogHelper.info("===AutoUsePatch：setBlocking： true===");
        }
        else {
            isBlocking = false;
            doOneAutoUseAction();

            LogHelper.info("===AutoUsePatch：setBlocking： false===");
        }
    }

    public static void clearTask() {
        taskList.clear();
    }


    public static void addNewTask(AbstractCard card) {
        LogHelper.info("===AutoUsePatch：新任务： card={}===",card.cardID);

        taskList.add(new AutoUseAction(card));
        doOneAutoUseAction();
    }


    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class OnAfterUseCard{
        @SpireInsertPatch(rloc = 1)
        public static void Insert(UseCardAction _inst){
            AutoUsePatch.setBlocking(false);
        }
    }


    private static void doOneAutoUseAction(){
        if (isBlocking || taskList.isEmpty()) {
            return;
        }

        AbstractGameAction action = taskList.remove(0);
        AbstractDungeon.actionManager.addToBottom(action);
        AutoUsePatch.setBlocking(true);

        if (action instanceof AutoUseAction){
            LogHelper.info("===InPlayerEndTurnPeriodPatch：addDelayAutoUseAction：处理一个：card={}===",((AutoUseAction)action).card.cardID);
        }
    }
}
