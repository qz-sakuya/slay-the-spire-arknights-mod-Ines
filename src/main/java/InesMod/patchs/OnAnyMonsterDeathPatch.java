package InesMod.patchs;

import InesMod.helpers.LogHelper;
import InesMod.powers.AbstractInesPower;
import InesMod.relics.AbstractInesRelic;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

/**
 *  为 power 添加 OnAnyMonsterDeathPatch 回调
 *  使任何单位可以监测任何怪物死亡
 *  对觉醒者和小黑有额外处理
 */
public class OnAnyMonsterDeathPatch {
    @SpirePatch(clz = AbstractMonster.class, method = "die",
            paramtypez = {boolean.class})
    public static class Fun1 {
        @SpirePrefixPatch
        public static void Prefix(AbstractMonster __instance, boolean triggerRelics) {
            if (!__instance.isDying) {
                Work(__instance);
            }
        }
    }

    @SpirePatch(clz = Darkling.class, method = "damage")
    public static class Fun2 {
        @SpireInsertPatch(rloc = 1)
        public static void Insertfix(Darkling __instance, DamageInfo info) {
            if (__instance.currentHealth <= 0 && !__instance.halfDead) {
                Work(__instance);
            }
        }
    }

    @SpirePatch(clz = AwakenedOne.class, method = "damage")
    public static class Fun3 {
        @SpireInsertPatch(rloc = 1)
        public static void Insertfix(AwakenedOne __instance, DamageInfo info) {
            if (__instance.currentHealth <= 0 && !__instance.halfDead) {
                Work(__instance);
            }
        }
    }

    public static void Work(AbstractMonster m){
        LogHelper.info("===OnAnyMonsterDeathPatch Work：监测到怪物死亡===");

        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof AbstractInesPower) {
                ((AbstractInesPower) power).onAnyMonsterDeath(m);
            }
        }

        for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
            for (AbstractPower power : mon.powers) {
                if (power instanceof AbstractInesPower) {
                    ((AbstractInesPower) power).onAnyMonsterDeath(m);
                }
            }
        }


    }
}
