package InesMod.patchs;


import InesMod.cards.AbstractInesCard;
import InesMod.helpers.LogHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;


/**
 * 使卡牌也获得 战斗开始时 的回调
 */
public class AtBattleStartPreDrawPatch {

    @SpirePatch(clz = AbstractPlayer.class,method = "applyStartOfCombatPreDrawLogic")
    public static class ApplyStartOfCombatPreDrawLogicPatch{
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer _inst){
            LogHelper.info("===AtBattleStartPreDrawPatch：触发===");

            // 触发自定义计数器
            ExhaustCountInCombatManager.set(0);

            // 触发自定义回调
            for (AbstractCard cardToCall : AbstractDungeon.player.hand.group) {
                if (cardToCall instanceof AbstractInesCard){
                    AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                    inesCard.atBattleStartPreDraw();
                }
            }

            for (AbstractCard cardToCall : AbstractDungeon.player.discardPile.group) {
                if (cardToCall instanceof AbstractInesCard){
                    AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                    inesCard.atBattleStartPreDraw();
                }
            }

            for (AbstractCard cardToCall : AbstractDungeon.player.drawPile.group) {
                if (cardToCall instanceof AbstractInesCard){
                    AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                    inesCard.atBattleStartPreDraw();
                }
            }

            for (AbstractCard cardToCall : AbstractDungeon.player.exhaustPile.group) {
                if (cardToCall instanceof AbstractInesCard){
                    AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                    inesCard.atBattleStartPreDraw();
                }
            }
        }
    }
}
