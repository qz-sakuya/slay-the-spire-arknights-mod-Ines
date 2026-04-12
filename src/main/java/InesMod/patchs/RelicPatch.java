package InesMod.patchs;


import InesMod.characters.Ines;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.*;

public class RelicPatch {


    // 禁用药丸，树枝，手里剑，苦无
    @SpirePatch(clz = AbstractDungeon.class,method = "initializeRelicList")
    public static class RelicListPatch{
        @SpirePostfixPatch
        public static void Postfix(AbstractDungeon _inst){
            // LogHelper.info("===RelicListPatch：Postfix===");
            if(AbstractDungeon.player instanceof Ines){
                AbstractDungeon.shopRelicPool.remove(OrangePellets.ID);
                AbstractDungeon.rareRelicPool.remove(DeadBranch.ID);
                AbstractDungeon.uncommonRelicPool.remove(Shuriken.ID);
                AbstractDungeon.uncommonRelicPool.remove(Kunai.ID);
            }
        }
    }
}
