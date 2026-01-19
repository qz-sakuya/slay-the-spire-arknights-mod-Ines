package InesMod.truth;

import InesMod.characters.Ines;


import InesMod.helpers.LogHelper;
import InesMod.interpret.InterpretOption;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.TopPanelGroup;
import basemod.TopPanelItem;
import basemod.patches.com.megacrit.cardcrawl.helpers.TopPanel.TopPanelHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import java.util.ArrayList;

public class TruthManager {
    // 实值通过InesSave进行存档
    public static int amount = 0;

    // TruthManager 是独立持续运行的，如果退出到主界面再返回，TruthManager 的所有变量值也不变

    // 因此，虚值在游戏重进时，需要清空。
    // 在本mod中，在移动到新房间后，才应用虚值
    public static int virtualAmount = 0;
    public static TruthTopItem truthTopItem = new TruthTopItem();



    public static int getTotalAmount(){
        return amount + virtualAmount;
    }

    public static void updateVirtual(int amt){
        virtualAmount += amt;
    }

    // 清空虚值
    public static void clearVirtual(){
        virtualAmount = 0;
    }

    // 应用虚值
    public static void applyVirtual(){
        LogHelper.info("===TruthManager:convertAllVirtual:应用虚值{}===",virtualAmount);
        amount += virtualAmount;
        if (amount < 0){
            amount = 0;
        }
        virtualAmount = 0;
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

    // 向顶部面板列表加入显示真相的组件
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
            if(TruthManager.getTotalAmount() >=8)
                valid = true;
            buttons.add(new InterpretOption(valid));
        }
    }


    @SpirePatch(clz = AbstractRelic.class, method = SpirePatch.CLASS)
    public static class RelicField {
        public static SpireField<Boolean> unRefractor = new SpireField<Boolean>(() -> false);
    }
}
