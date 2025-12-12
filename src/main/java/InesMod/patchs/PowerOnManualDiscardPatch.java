package InesMod.patchs;

import InesMod.enums.InesCardTags;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

/**
 *  触发 power 的 onManualDiscard 回调
 */
public class PowerOnManualDiscardPatch {
    @SpirePatch(clz = GameActionManager.class, method = "incrementDiscard",
                paramtypez = {boolean.class})
    public static class Fun {
        @SpirePostfixPatch
        public static void Postfix(boolean endOfTurn) { // 原方法是static，不用加instance参数

            if (!AbstractDungeon.actionManager.turnHasEnded && !endOfTurn) {
                for (AbstractPower p : AbstractDungeon.player.powers) {
                    if (p instanceof AbstractInesPower) {
                        ((AbstractInesPower)p).onManualDiscard();
                    }
                }
            }
        }
    }
}
