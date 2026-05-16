package InesMod.patchs;


import InesMod.action.TryClearDefenseArtilleryMeterUponAction;
import InesMod.enums.InesCardTags;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

/**
 * 玩家回合开始时 需要处理的内容
 */
public class OnPlayerTurnStartPatch {
    private static UIStrings retainThisTurnStrings = null;

    @SpirePatch(clz = AbstractCreature.class,method = "applyStartOfTurnPowers")
    public static class StartOfTurn{
        @SpirePostfixPatch
        public static void Postfix(AbstractCreature _inst){
            if (_inst == AbstractDungeon.player){
                LogHelper.info("===OnPlayerTurnStartPatch：触发===");

                // 重置“在本回合保留。”词条
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    resetCardsRetainThisTurn(c);
                }

                for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                    resetCardsRetainThisTurn(c);
                }

                for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                    resetCardsRetainThisTurn(c);
                }

                for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
                    resetCardsRetainThisTurn(c);
                }

                // 尝试清除城防炮特效
                TryClearDefenseArtilleryMeterUponAction.Work(null);
            }
        }
    }

    // 如果卡牌具有“在本回合保留。”，则重置
    static private void resetCardsRetainThisTurn(AbstractCard card) {
        if (retainThisTurnStrings == null) {
            retainThisTurnStrings = CardCrawlGame.languagePack.getUIString(PathHelper.nameToId("RetainCardsThisTurnAction"));
        }

        if (card.tags.contains(InesCardTags.RetainThisTurn)) {
            // 删除tag（文本由 CardTagTextPatch 处理）
            card.tags.remove(InesCardTags.RetainThisTurn);
            card.initializeDescription();

            // 不再保留
            card.retain = false;
        }
    }
}
