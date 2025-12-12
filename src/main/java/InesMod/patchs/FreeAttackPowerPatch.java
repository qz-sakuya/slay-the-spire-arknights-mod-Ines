package InesMod.patchs;

import InesMod.helpers.LoggerHelper;
import InesMod.helpers.PathHelper;
import InesMod.modcore.InesModMain;
import InesMod.powers.InvisibilityPower;
import InesMod.powers.NoInvisibilityPower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.FreeAttackPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

/**
 *  给 免费攻击 能力进行patch
 *  使其能够刷新文本
 */
public class FreeAttackPowerPatch {

    @SpirePatch(clz = FreeAttackPower.class, method = "onUseCard")
    public static class Fun {
        @SpirePostfixPatch
        public static void Postfix(FreeAttackPower __instance, AbstractCard card, UseCardAction action) {
            LoggerHelper.info("===FreeAttackPowerPatch Fun：begin===");
            __instance.updateDescription();
        }

    }


}
