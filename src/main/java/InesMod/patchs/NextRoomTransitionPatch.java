package InesMod.patchs;

import InesMod.cards.AbstractInesCard;
import InesMod.helpers.LogHelper;
import InesMod.powers.AbstractInesPower;
import InesMod.truth.TruthManager;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

/**
 *  patch 移动到下一个房间
 *  加入应用真相的虚值
 */
public class NextRoomTransitionPatch {


    @SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition",
            paramtypez = {SaveFile.class})
    public static class Fun1 {
        @SpirePrefixPatch
        public static void Prefix(AbstractDungeon __instance, SaveFile saveFile) {
            LogHelper.info("===NextRoomTransitionPatch：begin===");
            TruthManager.applyVirtual();
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition",
            paramtypez = {})
    public static class Fun2 {
        @SpirePrefixPatch
        public static void Prefix(AbstractDungeon __instance) {
            LogHelper.info("===NextRoomTransitionPatch：begin===");
            TruthManager.applyVirtual();
        }
    }

}
