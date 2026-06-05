package InesMod.patchs;


import InesMod.cards.AbstractInesCard;
import InesMod.helpers.LogHelper;
import InesMod.helpers.ResetHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;


/**
 * 战斗结束时 需要处理的内容
 */
public class atBattleEndPatch {

    @SpirePatch(clz = AbstractPlayer.class, method = "onVictory")
    public static class ApplyStartOfCombatPreDrawLogicPatch{
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer _inst){
            LogHelper.info("===atBattleEndPatch：触发===");

            ResetHelper.resetOnce();

            // 使卡牌也获得 战斗结束时 的回调
            for (AbstractCard cardToCall : AbstractDungeon.player.hand.group) {
                if (cardToCall instanceof AbstractInesCard){
                    AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                    inesCard.atBattleEnd();
                }
            }

            for (AbstractCard cardToCall : AbstractDungeon.player.discardPile.group) {
                if (cardToCall instanceof AbstractInesCard){
                    AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                    inesCard.atBattleEnd();
                }
            }

            for (AbstractCard cardToCall : AbstractDungeon.player.drawPile.group) {
                if (cardToCall instanceof AbstractInesCard){
                    AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                    inesCard.atBattleEnd();
                }
            }

            for (AbstractCard cardToCall : AbstractDungeon.player.exhaustPile.group) {
                if (cardToCall instanceof AbstractInesCard){
                    AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                    inesCard.atBattleEnd();
                }
            }

            // 包括牌组
            for (AbstractCard cardToCall : AbstractDungeon.player.masterDeck.group) {
                if (cardToCall instanceof AbstractInesCard){
                    AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                    inesCard.atBattleEnd();
                }
            }
        }
    }
}
