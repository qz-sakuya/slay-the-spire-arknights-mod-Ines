package InesMod.patchs;


import InesMod.dungeons.LevelChapter10;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.vfx.campfire.CampfireBubbleEffect;

import java.util.Iterator;


public class CampFireEffectPatch {

    // LevelChapter10 不显示火堆火焰特效
    @SpirePatch(clz = CampfireUI.class,method = "updateFire")
    public static class Fun{
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(CampfireUI _inst){
            if(CardCrawlGame.dungeon instanceof LevelChapter10){
                return SpireReturn.Return(null);
            }


            return SpireReturn.Continue();
        }
    }
}
