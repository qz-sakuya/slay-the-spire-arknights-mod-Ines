package InesMod.patchs;

import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import InesMod.powers.player.InvisibilityPower;
import InesMod.powers.player.NoInvisibilityPower;
import InesMod.relics.AbstractInesRelic;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

/**
 *  新增自定义回调：进入damage()后，decrementBlock()之前
 *  包括 OnAttackBeforeBlock 和 OnAttackedBeforeBlock
 *  玩家和敌人都要patch
 */
public class OnAttackBeforeBlockPatch {
    public static final String ID = PathHelper.nameToId(OnAttackBeforeBlockPatch.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID); // 从游戏系统读取本地化资源


    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class Fun1 {
        @SpirePrefixPatch
        public static void Prefix(AbstractPlayer __instance, DamageInfo info) {
            int damage = info.output;
            int currentBlock = __instance.currentBlock;

            if (info.owner == __instance) {
                for (AbstractRelic r : __instance.relics) {
                    if (r instanceof AbstractInesRelic){
                        damage = ((AbstractInesRelic) r).OnAttackBeforeBlock(info, damage, currentBlock);
                    }
                }
            }
            if (info.owner != null) {
                for (AbstractPower p : info.owner.powers) {
                    if (p instanceof AbstractInesPower){
                        damage = ((AbstractInesPower) p).OnAttackBeforeBlock(info, damage, currentBlock);
                    }
                }
            }

            for (AbstractRelic r : __instance.relics) {
                if (r instanceof AbstractInesRelic){
                    damage = ((AbstractInesRelic) r).OnAttackedBeforeBlock(info, damage, currentBlock);
                }
            }

            for (AbstractPower p : __instance.powers) {
                if (p instanceof AbstractInesPower){
                    damage = ((AbstractInesPower) p).OnAttackedBeforeBlock(info, damage, currentBlock);
                }
            }

            // 取巧：将伤害值放回去，通过同一个实例让源函数读到
            info.output = damage;
        }
    }


    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class Fun2 {
        @SpirePrefixPatch
        public static void Prefix(AbstractMonster __instance, DamageInfo info) {
            int damage = info.output;
            int currentBlock = __instance.currentBlock;

            
            if (info.owner == AbstractDungeon.player) {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    if (r instanceof AbstractInesRelic){
                        damage = ((AbstractInesRelic) r).OnAttackBeforeBlock(info, damage, currentBlock);
                    }
                }
            }
            if (info.owner != null) {
                for (AbstractPower p : info.owner.powers) {
                    if (p instanceof AbstractInesPower){
                        damage = ((AbstractInesPower) p).OnAttackBeforeBlock(info, damage, currentBlock);
                    }
                }
            }

            for (AbstractPower p : __instance.powers) {
                if (p instanceof AbstractInesPower){
                    damage = ((AbstractInesPower) p).OnAttackedBeforeBlock(info, damage, currentBlock);
                }
            }

            // 取巧：将伤害值放回去，通过同一个实例让源函数读到
            info.output = damage;
        }
    }

}
