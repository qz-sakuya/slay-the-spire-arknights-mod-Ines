package InesMod.patchs;

import InesMod.cards.AbstractInesCard;
import InesMod.helpers.LogHelper;
import InesMod.monsters.AbstractInesMonster;
import InesMod.powers.AbstractInesPower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.unique.SummonGremlinAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

/**
 *  为 power 添加和遗物类似的 onSpawnMonster 回调
 *  监测： 战斗中生成怪物
 */
public class OnSpawnMonsterPatch {
    @SpirePatch(clz = SpawnMonsterAction.class, method = "update")
    public static class Fun1 {
        @SpirePrefixPatch
        public static void Prefix(SpawnMonsterAction __instance) {
            boolean used = ReflectionHacks.getPrivate(__instance, SpawnMonsterAction.class, "used");
            if (!used) {
                AbstractMonster m = ReflectionHacks.getPrivate(__instance, SpawnMonsterAction.class, "m");
                Work(m);
            }
        }
    }

    @SpirePatch(clz = SummonGremlinAction.class, method = "SummonGremlinAction")
    public static class Fun2 {
        @SpirePostfixPatch
        public static void Postfix(SummonGremlinAction __instance, AbstractMonster[] gremlins) {
            AbstractMonster m = ReflectionHacks.getPrivate(__instance, SummonGremlinAction.class, "m");
            Work(m);
        }
    }

    @SpirePatch(clz = Darkling.class, method = "takeTurn")
    public static class Fun3 {
        @SpirePostfixPatch
        public static void Postfix(Darkling __instance) {
            if (__instance.nextMove == 5) {
                Work(__instance);
            }
        }
    }

    private static void Work(AbstractMonster m){
        LogHelper.info("===OnSpawnMonsterPatch Work：监测到怪物生成===");

        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof AbstractInesPower) {
                ((AbstractInesPower) power).onSpawnMonster(m);
            }
        }

        for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
            for (AbstractPower power : mon.powers) {
                if (power instanceof AbstractInesPower) {
                    ((AbstractInesPower) power).onSpawnMonster(m);
                }
            }
        }
    }
}
