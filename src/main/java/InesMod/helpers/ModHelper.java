package InesMod.helpers;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.FtueTip;

import java.io.IOException;

public class ModHelper {


    public static String nameToId(String name) {
        return "InesMod:" + name;
    }

    public static String idToName(String id) {
        return id.replace("InesMod:","");
    }



}
