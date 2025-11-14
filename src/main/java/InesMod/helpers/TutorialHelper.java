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

public class TutorialHelper {
    private static final UIStrings tips = CardCrawlGame.languagePack.getUIString(PathHelper.nameToId("Tips"));


    public static void playTutorial1(AbstractCreature p) {
        if (!ModConfig.tutorialClosed1) {
            float x = p.hb.cX + p.hb.width + 140.0F * Settings.scale;
            float y = p.hb.cY;
            AbstractDungeon.ftue = new FtueTip(tips.TEXT[0], tips.TEXT[1]+tips.TEXT[2], x, y, FtueTip.TipType.CREATURE);
            ReflectionHacks.setPrivate(AbstractDungeon.ftue, FtueTip.class, "m", p);
            ModConfig.tutorialClosed1 = true;

            try {
                SpireConfig config = new SpireConfig("InesModArknights", "Common");
                config.setBool(PathHelper.nameToId("TUTORIAL_CLOSED_1"), true);
                config.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
