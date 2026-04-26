package InesMod.action;

import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.LogHelper;
import InesMod.patchs.AutoUsePatch;
import InesMod.patchs.InPlayerEndTurnPeriodPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

/**
 * 自动打出
 */
public class AutoUseAction extends AbstractGameAction {
    public final AbstractCard card;
//    public boolean delayOnce = false;
    public AbstractGameAction actionWhenFail = null;



    public AutoUseAction(AbstractCard card) {
        this.card = card;
        if (card instanceof AbstractInesCard){
            this.actionWhenFail = ((AbstractInesCard)card).actionWhenAutoUseFail;
        }
    }

    public void update() {
        if (card == null
            || !AbstractDungeon.player.hand.group.contains(card)) {
            this.isDone = true;
            return;
        }


        LogHelper.info("===AutoUseAction：开始，card={}===",card.cardID);
//
//        if (this.delayOnce) {
//            LogHelper.info("===AutoUseAction：delayOnce延迟1次，card={}===",card.cardID);
//            addToBot(new AutoUseAction(card, actionWhenFail));
//            this.isDone = true;
//            return;
//        }
//
//        if (InPlayerEndTurnPeriodPatch.inPlayerEndTurnPeriod) {
//            LogHelper.info("===AutoUseAction：回合结束状态延迟1次，card={}===",card.cardID);
//            AutoUsePatch.add(new AutoUseAction(card, actionWhenFail));
//            this.isDone = true;
//            return;
//        }



        if (!this.card.hasEnoughEnergy() || !this.card.cardPlayable(null)){
            LogHelper.info("===AutoUseAction：无法打出，card={}===",card.cardID);

            AutoUsePatch.setBlocking(false);

            if (actionWhenFail != null) {
                addToTop(actionWhenFail);
            }
        }
        else {
            // 立即消耗能量打出
            this.card.applyPowers();
            LogHelper.info("===AutoUseAction：自动打出，card={}===",card.cardID);
            addToTop(new NewQueueCardAction(this.card, true, true, true));
            if (!this.card.freeToPlay()) {
                addToTop(new LoseEnergyAction(this.card.cost));
            }



//            // 使其它 AutoUse 相关 Action 延后
//            ArrayList<AbstractGameAction> actions = AbstractDungeon.actionManager.actions;
//            for (AbstractGameAction action : actions) {
//                if (action instanceof AutoUseAction
//                        && action != this
//                        && !action.isDone
//                ) {
//                    AutoUseAction autoUseAction = (AutoUseAction) action;
//                    InPlayerEndTurnPeriodPatch.delayAutoUseActionList.add(new AutoUseAction(autoUseAction.card, autoUseAction.actionWhenFail));
//                    autoUseAction.isDone = true;
//                }
//            }
        }

        this.isDone = true;
    }




}
