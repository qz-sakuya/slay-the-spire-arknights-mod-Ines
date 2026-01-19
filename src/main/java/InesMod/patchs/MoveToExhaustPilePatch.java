package InesMod.patchs;

import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 *  给 moveToExhaustPile 方法进行patch
 *  触发自定义回调
 */
public class MoveToExhaustPilePatch {

    @SpirePatch(clz = CardGroup.class, method = "moveToExhaustPile")
    public static class Fun {
        @SpirePostfixPatch
        public static void Postfix(CardGroup __instance, AbstractCard c) {
            Work(c);
        }
    }

    public static void Work(AbstractCard c) {
        // 触发自定义回调
        if (AbstractDungeon.player instanceof Ines) {
            ((Ines)AbstractDungeon.player).onExhaust(c);
        }

        for (AbstractCard cardToCall : AbstractDungeon.player.hand.group) {
            if (cardToCall instanceof AbstractInesCard){
                AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                inesCard.onExhaust(c);
            }
        }

        for (AbstractCard cardToCall : AbstractDungeon.player.discardPile.group) {
            if (cardToCall instanceof AbstractInesCard){
                AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                inesCard.onExhaust(c);
            }
        }

        for (AbstractCard cardToCall : AbstractDungeon.player.drawPile.group) {
            if (cardToCall instanceof AbstractInesCard){
                AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                inesCard.onExhaust(c);
            }
        }

        for (AbstractCard cardToCall : AbstractDungeon.player.exhaustPile.group) {
            if (cardToCall instanceof AbstractInesCard){
                AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                inesCard.onExhaust(c);
            }
        }
    }
}
