package InesMod.patchs;

import InesMod.cards.AbstractInesCard;
import InesMod.modcore.InesModMain;
import InesMod.powers.AbstractInesPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 *  给 CardGroup 的 add 方法进行patch
 *  如果 CardGroup 是手牌，触发 自动打出 效果
 *  使用此函数的例子：秘密武器
 */
public class CardGroupAddPatch {


    @SpirePatch(clz = CardGroup.class, method = "addToTop")
    public static class AddToTop {
        @SpirePrefixPatch
        public static void Prefix(CardGroup __instance, AbstractCard c) {
            InesModMain.logger.info("===CardGroupAddPatch AddToTop：begin===");
            Work(__instance,c);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToBottom")
    public static class AddToBottom {
        @SpirePrefixPatch
        public static void Prefix(CardGroup __instance, AbstractCard c) {
            InesModMain.logger.info("===CardGroupAddPatch AddToBottom：begin===");
            Work(__instance,c);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToRandomSpot")
    public static class AddToRandomSpot {
        @SpirePrefixPatch
        public static void Prefix(CardGroup __instance, AbstractCard c) {
            InesModMain.logger.info("===CardGroupAddPatch AddToRandomSpot：begin===");
            Work(__instance,c);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToHand")
    public static class AddToHand {
        @SpirePrefixPatch
        public static void Prefix(CardGroup __instance, AbstractCard c) {
            InesModMain.logger.info("===CardGroupAddPatch AddToHand：begin===");
            Work(__instance,c);
        }

    }

    public static void Work(CardGroup __instance, AbstractCard c) {
        InesModMain.logger.info("===CardGroupAddPatch：被加入的卡牌ID{}===", c.cardID);
        InesModMain.logger.info("===CardGroupAddPatch：牌被加入的位置{}===", __instance.type);

        // 触发自动打出
        if (c instanceof AbstractInesCard ){
            AbstractInesCard tmp = (AbstractInesCard)c;

            if (__instance.type == CardGroup.CardGroupType.HAND) {
                if (tmp.lastAddedTo != CardGroup.CardGroupType.HAND) {
                    tmp.autoUse();
                }
                else{
                    InesModMain.logger.info("===CardGroupAddPatch：从手牌回到手牌，跳过===");
                }
            }
            else{
                InesModMain.logger.info("===CardGroupAddPatch：被 add 的 CardGroup 不是手牌，跳过===");
            }
        }

        // 更新卡牌的位置
        if (c instanceof AbstractInesCard ){
            AbstractInesCard tmp = (AbstractInesCard)c;
            if (__instance.type == CardGroup.CardGroupType.HAND ||
                    __instance.type == CardGroup.CardGroupType.DRAW_PILE ||
                    __instance.type == CardGroup.CardGroupType.DISCARD_PILE ||
                    __instance.type == CardGroup.CardGroupType.EXHAUST_PILE) {

                tmp.lastAddedTo = __instance.type;
                InesModMain.logger.info("===CardGroupAddPatch：更新卡牌lastAddedTo为: {}===", tmp.lastAddedTo);
            }
        }

        // 触发自定义回调
        for (AbstractPower powerToCall : AbstractDungeon.player.powers) {
            if (powerToCall instanceof AbstractInesPower){
                AbstractInesPower inesPower = (AbstractInesPower)powerToCall;
                inesPower.onCardMove(c, __instance.type);
            }
        }

        for (AbstractCard cardToCall : AbstractDungeon.player.hand.group) {
            if (cardToCall instanceof AbstractInesCard){
                AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                inesCard.onCardMove(c, __instance.type);
            }
        }

        for (AbstractCard cardToCall : AbstractDungeon.player.discardPile.group) {
            if (cardToCall instanceof AbstractInesCard){
                AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                inesCard.onCardMove(c, __instance.type);
            }
        }

        for (AbstractCard cardToCall : AbstractDungeon.player.drawPile.group) {
            if (cardToCall instanceof AbstractInesCard){
                AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                inesCard.onCardMove(c, __instance.type);
            }
        }

        for (AbstractCard cardToCall : AbstractDungeon.player.exhaustPile.group) {
            if (cardToCall instanceof AbstractInesCard){
                AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                inesCard.onCardMove(c, __instance.type);
            }
        }
    }
}
