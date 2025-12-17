package InesMod.patchs;

import InesMod.helpers.LogHelper;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;

/**
 *  修复 手牌选择框 在 numCardsToSelect > 1、anyNumber为真、canPickZero为假 的情况下，只选1张牌，无法确定的bug
 */
public class HandCardSelectScreenBugFixPatch {

    @SpirePatch(clz = HandCardSelectScreen.class, method = "refreshSelectedCards")
    public static class Fun {
        @SpirePostfixPatch
        public static void Postfix(HandCardSelectScreen __instance) {
            LogHelper.info("===InesMod: HandCardSelectScreenBugFixPatch: Postfix===");

            boolean anyNumber = ReflectionHacks.getPrivate(__instance, HandCardSelectScreen.class, "anyNumber");
            if (__instance.selectedCards.size() == 1 && anyNumber && !__instance.canPickZero) {
                __instance.button.enable();
            }
        }
    }


}
