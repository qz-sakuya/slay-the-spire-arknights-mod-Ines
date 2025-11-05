package InesMod.patchs;

import InesMod.helpers.ModHelper;
import InesMod.modcore.InesModMain;
import InesMod.powers.AgentVanguardPower;
import InesMod.powers.InvisibilityPower;
import InesMod.powers.NoInvisibilityPower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

/**
 *  给 添加能力 方法进行patch
 *  在方法开头，加入不能获取隐匿的检测
 */
public class ApplyPowerActionPatch {
    public static final String ID = ModHelper.nameToId(ApplyPowerActionPatch.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID); // 从游戏系统读取本地化资源


    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class Fun {
        @SpirePrefixPatch
        public static void Prefix(ApplyPowerAction __instance) {
            AbstractPower powerToApply = ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "powerToApply");
            InesModMain.logger.info("===ApplyPowerActionPatch Fun：被添加的能力ID{}===", powerToApply.ID);
            InesModMain.logger.info("===ApplyPowerActionPatch Fun：被添加的能力层数{}===", powerToApply.amount);

            // 如果不能获得隐匿，且是添加隐匿，则禁止
            AbstractPower noInvisibilityPower = __instance.source.getPower(NoInvisibilityPower.ID);
            if (noInvisibilityPower != null && powerToApply.ID.equals(InvisibilityPower.ID)) {
                InesModMain.logger.info("===ApplyPowerActionPatch Fun：阻止添加隐匿===");

                noInvisibilityPower.flash();
                __instance.target = null; // 将目标设为空，后续就不会添加

                // 对话气泡
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY - 30.0F, 3.0F, uiStrings.TEXT[0], true));
            }
        }



    }


}
