package InesMod.patchs;

import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.powers.player.CageOfWarsPower;
import InesMod.powers.player.InvisibilityPower;
import InesMod.powers.player.NoInvisibilityPower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import java.util.ArrayList;

/**
 *  战争囚笼 限制攻击目标
 */
public class CageOfWarsPatch {
    public static final String ID = PathHelper.nameToId(CageOfWarsPatch.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID); // 从游戏系统读取本地化资源



    @SpirePatch(clz = AbstractCard.class, method = "cardPlayable")
    public static class Fun {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(AbstractCard __instance, AbstractMonster m) {
            if (__instance.type != AbstractCard.CardType.ATTACK) {
                return SpireReturn.Continue();
            }

            if (__instance.target == AbstractCard.CardTarget.ALL_ENEMY || __instance.target == AbstractCard.CardTarget.ALL || __instance.target == AbstractCard.CardTarget.NONE) {
                return SpireReturn.Continue();
            }

            if (m == null) {
                return SpireReturn.Continue();
            }


            ArrayList<AbstractMonster> targetMonsterList = new ArrayList<>();

            for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                if (mo != null && !mo.isDeadOrEscaped() && mo.hasPower(CageOfWarsPower.ID)) {
                    targetMonsterList.add(mo);
                }
            }

            // 没有怪物有 战争囚笼 这个power，跳过
            if (targetMonsterList.isEmpty()) {
                return SpireReturn.Continue();
            }



            if (!targetMonsterList.contains(m)) {
                __instance.cantUseMessage = uiStrings.TEXT[0];
                return SpireReturn.Return(Boolean.valueOf(false));
            }

            return SpireReturn.Continue();
        }

    }
}
