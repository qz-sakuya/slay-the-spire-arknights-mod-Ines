package InesMod.patchs;

import InesMod.helpers.LogHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.watcher.FreeAttackPower;

/**
 *  给 免费攻击 能力进行patch
 *  使其能够刷新文本
 */
public class FreeAttackPowerPatch {

    @SpirePatch(clz = FreeAttackPower.class, method = "onUseCard")
    public static class Fun {
        @SpirePostfixPatch
        public static void Postfix(FreeAttackPower __instance, AbstractCard card, UseCardAction action) {
            LogHelper.info("===FreeAttackPowerPatch Fun：begin===");
            __instance.updateDescription();
        }

    }


}
