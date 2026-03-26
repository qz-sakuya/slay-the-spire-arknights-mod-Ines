package InesMod.patchs;

import InesMod.cards.AbstractInesCard;
import InesMod.helpers.LogHelper;
import InesMod.powers.AbstractInesPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

/**
 *  触发 power 和卡牌的 onManualDiscard 回调
 */
public class OnManualDiscardPatch {
    @SpirePatch(clz = GameActionManager.class, method = "incrementDiscard",
                paramtypez = {boolean.class})
    public static class Fun {
        @SpirePostfixPatch
        public static void Postfix(boolean endOfTurn) { // 原方法是static，不用加instance参数
            // 非回合结束时弃牌
            if (!AbstractDungeon.actionManager.turnHasEnded && !endOfTurn) {
                // 获取弃掉的牌的信息（比较取巧，没有大量测试会不会有bug）
                ArrayList<AbstractCard> group =  AbstractDungeon.player.discardPile.group;
                AbstractCard c = group.get(group.size() - 1);
                LogHelper.info("===InesMod: OnManualDiscardPatch: 上一张弃的牌是: id={}===",c.cardID);

                for (AbstractPower p : AbstractDungeon.player.powers) {
                    if (p instanceof AbstractInesPower) {
                        ((AbstractInesPower)p).onManualDiscard(c);
                    }
                }

                for (AbstractCard cardToCall : AbstractDungeon.player.hand.group) {
                    if (cardToCall instanceof AbstractInesCard){
                        AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                        inesCard.onManualDiscard(c);
                    }
                }

                for (AbstractCard cardToCall : AbstractDungeon.player.discardPile.group) {
                    if (cardToCall instanceof AbstractInesCard){
                        AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                        inesCard.onManualDiscard(c);
                    }
                }

                for (AbstractCard cardToCall : AbstractDungeon.player.drawPile.group) {
                    if (cardToCall instanceof AbstractInesCard){
                        AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                        inesCard.onManualDiscard(c);
                    }
                }

                for (AbstractCard cardToCall : AbstractDungeon.player.exhaustPile.group) {
                    if (cardToCall instanceof AbstractInesCard){
                        AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                        inesCard.onManualDiscard(c);
                    }
                }
            }


        }
    }
}
