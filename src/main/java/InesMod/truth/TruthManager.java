package InesMod.truth;

import InesMod.characters.Ines;


import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.TopPanelGroup;
import basemod.TopPanelItem;
import basemod.patches.com.megacrit.cardcrawl.helpers.TopPanel.TopPanelHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import java.util.ArrayList;

public class TruthManager {
    public static int truthAmount;
    public static TruthTopItem truthTopItem = new TruthTopItem();

    public static void gain(int amt){
        if(amt>0){
            truthAmount += amt;
        }
    }

    public static void lose(int amt){
        truthAmount -= amt;
        if(truthAmount <= 0){
            truthAmount = 0;
        }
    }

    public static void setTopPanelItem(){
        if(AbstractDungeon.player instanceof Ines){
            if(!addedItem())
                BaseMod.addTopPanelItem(truthTopItem);
        }
        else{
            BaseMod.removeTopPanelItem(truthTopItem);
        }
    }

    private static boolean addedItem(){
        ArrayList<TopPanelItem> items = ReflectionHacks.getPrivate(TopPanelHelper.topPanelGroup, TopPanelGroup.class,"topPanelItems");
        return items.contains(truthTopItem);
    }




    // 显示解析真相的按钮
    @SpirePatch(clz = CampfireUI.class, method = "initializeButtons")
    public static class AddTearChargePatch {
        @SpireInsertPatch(rloc = 33,localvars = {"buttons"})
        public static void Insert(CampfireUI _inst, ArrayList<AbstractCampfireOption> buttons) {
            if(!(AbstractDungeon.player instanceof Ines) )
                return;
            boolean valid = false;
            if(TruthManager.truthAmount >=8)
                valid = true;
            // buttons.add(new RebuildOption(valid));
        }
    }
}
