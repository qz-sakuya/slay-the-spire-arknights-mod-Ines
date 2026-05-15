package InesMod.vfx;

import InesMod.characters.Ines;
import InesMod.helpers.LogHelper;
import InesMod.interpret.InterpretOption;
import InesMod.truth.TruthTopItem;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.TopPanelGroup;
import basemod.TopPanelItem;
import basemod.patches.com.megacrit.cardcrawl.helpers.TopPanel.TopPanelHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;


/**
 * 城防炮进度条特效管理器
 */
public class DefenseArtilleryMeterUponManager {

    // 该 Manager 是独立持续运行的


    public static AbstractGameEffect effect = null;


    public static void clearEffect(){
        LogHelper.info("===InesMod：DefenseArtilleryMeterUponManager：clearEffect===");
        if(effect != null){
            effect.duration = 0F;
        }
        effect = null;
    }

    public static void initEffect(int type){
        if(effect == null){
            setEffect(type);
        }
    }

    public static void setEffect(int type){
        LogHelper.info("===InesMod：DefenseArtilleryMeterUponManager：setEffect：type={}===", type);
        if(type < 0 || type > 4){
            return;
        }

        clearEffect();

        // 获取中心坐标
        float centerX = (float) Settings.WIDTH / 2.0F;
        float centerY = (float) Settings.HEIGHT / 2.0F;

        effect = new DefenseArtilleryMeterUponEffect(type, centerX, centerY * 2.0F * 0.8F, Settings.scale * 0.3F);
        AbstractDungeon.effectsQueue.add(effect);
    }
}
