package InesMod.patchs;


import InesMod.characters.Ines;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Kunai;
import com.megacrit.cardcrawl.relics.OrangePellets;
import com.megacrit.cardcrawl.relics.Shuriken;

public class RelicPatch {


    // 禁用药丸
    @SpirePatch(clz = AbstractDungeon.class,method = "initializeRelicList")
    public static class RelicListPatch{
        public static void Postfix(AbstractDungeon _inst){
            if(AbstractDungeon.player instanceof Ines){
                AbstractDungeon.uncommonRelicPool.remove(OrangePellets.ID);
            }
        }
    }
}
