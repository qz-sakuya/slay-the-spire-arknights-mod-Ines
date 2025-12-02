package InesMod.patchs;

import InesMod.enums.InesCardTags;
import InesMod.helpers.PathHelper;
import InesMod.modcore.InesModMain;
import InesMod.powers.InvisibilityPower;
import InesMod.powers.NoInvisibilityPower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

/**
 *  给 initializeDescription 方法进行patch
 *  在方法开头，加入添加 自定义tag文本 的操作
 *  （这么做的目的是，兼容部分卡牌的 EXTENDED_DESCRIPTION）
 *  不应该在其他任何地方再向 rawDescription 添加 tag文本
 */
public class AddCardTagDescriptionPatch {
    public static final String ID = PathHelper.nameToId("CardTags");
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);


    @SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
    public static class Fun {
        @SpirePrefixPatch
        public static void Prefix(AbstractCard __instance) {
            // 在本回合保留 tag
            if (__instance.tags.contains(InesCardTags.RetainThisTurn)) {
                __instance.rawDescription += uiStrings.TEXT[0];
            }
        }
    }


}
