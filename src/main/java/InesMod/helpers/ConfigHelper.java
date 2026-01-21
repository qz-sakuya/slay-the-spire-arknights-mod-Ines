package InesMod.helpers;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import java.io.IOException;
import java.util.Properties;

import static InesMod.helpers.LogHelper.FORCE_ENABLE_INFO;

public class ConfigHelper {
    // ---可调整的config---
    public static boolean banExtraEnding = false;
    public static boolean dontShowLoggerInfo = false;

    // ---不可调整的config---
    public static boolean tutorialClosed1 = false;


    public static SpireConfig config = null;
    private static final Properties defaultSetting = new Properties();
    private static ModPanel settingsPanel;


    // 加载所有config，包括可调整的和不可调整的
    public static void initModSettings() {
        defaultSetting.setProperty(PathHelper.nameToId("BAN_EXTRA_ENDING"), String.valueOf(banExtraEnding));
        defaultSetting.setProperty(PathHelper.nameToId("DONT_SHOW_LOGGER_INFO"), String.valueOf(dontShowLoggerInfo));

        defaultSetting.setProperty(PathHelper.nameToId("TUTORIAL_CLOSED_1"), String.valueOf(tutorialClosed1));

        try {
            config = new SpireConfig("InesModArknights", "Common", defaultSetting);
            config.load();

            banExtraEnding = config.getBool(PathHelper.nameToId("BAN_EXTRA_ENDING"));
            dontShowLoggerInfo = config.getBool(PathHelper.nameToId("DONT_SHOW_LOGGER_INFO"));

            tutorialClosed1 = config.getBool(PathHelper.nameToId("TUTORIAL_CLOSED_1"));

            LogHelper.info("===加载config: banExtraEnding:{}===",banExtraEnding);
            LogHelper.info("===加载config: tutorialClosed1:{}===",tutorialClosed1);

        } catch (Exception e) {
            LogHelper.info("===加载config失败{}===",e.getLocalizedMessage());
        }
    }

    // 加载config调整界面
    public static void initModConfigMenu() {
        settingsPanel = new ModPanel();
        addEnableMenu();
        String modConfDesc = (CardCrawlGame.languagePack.getUIString(PathHelper.nameToId("Config"))).TEXT[0];
        Texture badge = ImageMaster.loadImage("InesModResources/img/char/small_orb.png");
        BaseMod.registerModBadge(badge, "InesMod（伊内丝）", "轻千轻子Sakuya", modConfDesc, settingsPanel);
    }

    private static void addEnableMenu() {
        UIStrings uis = CardCrawlGame.languagePack.getUIString(PathHelper.nameToId("Config"));

        // 设置1按钮
        ModLabeledToggleButton btn1 = new ModLabeledToggleButton(uis.TEXT[1], 350.0F, 800.0F, Settings.CREAM_COLOR, FontHelper.charDescFont, banExtraEnding, settingsPanel, modLabel -> {
        }, modToggleButton -> {
            banExtraEnding = modToggleButton.enabled;
            config.setBool(PathHelper.nameToId("BAN_EXTRA_ENDING"), banExtraEnding);
            try {
                config.save();
            } catch (IOException e) {
                LogHelper.info("===save config credit failed{}===",e.getLocalizedMessage());
            }
        });
        settingsPanel.addUIElement(btn1);

        // 设置2按钮
        if(!FORCE_ENABLE_INFO){
            ModLabeledToggleButton btn2 = new ModLabeledToggleButton(uis.TEXT[2], 350.0F, 300.0F, Settings.CREAM_COLOR, FontHelper.charDescFont, banExtraEnding, settingsPanel, modLabel -> {
            }, modToggleButton -> {
                dontShowLoggerInfo = modToggleButton.enabled;
                config.setBool(PathHelper.nameToId("DONT_SHOW_LOGGER_INFO"), dontShowLoggerInfo);
                try {
                    config.save();
                } catch (IOException e) {
                    LogHelper.info("===save config credit failed{}===",e.getLocalizedMessage());
                }
            });
            settingsPanel.addUIElement(btn2);
        }


    }
}
