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
    // 禁用额外层级（优先级大于固定额外层级）
    public static boolean banExtraLevel = false;

    public static boolean dontShowLoggerInfo = false;

    // 是否固定额外层级
    public static boolean setExtraLevelBoss = false; // TODO：未添加按钮

    // 固定额外层级为
    // 0 -> Chapter 10
    // 1 -> Chapter 11
    // 2 -> Chapter 12
    // 3 -> Chapter 13
    // -1 -> Boss Rush
    public static int setExtraLevelBossTo = 0; // TODO：未添加按钮

    // ---不可调整的config---
    public static boolean tutorialClosed1 = false;


    public static SpireConfig config = null;
    private static final Properties defaultSetting = new Properties();
    private static ModPanel settingsPanel;


    // 加载所有config，包括可调整的和不可调整的
    public static void initModSettings() {
        // 默认值
        defaultSetting.setProperty(PathHelper.nameToId("BAN_EXTRA_LEVEL"), String.valueOf(banExtraLevel));
        defaultSetting.setProperty(PathHelper.nameToId("DONT_SHOW_LOGGER_INFO"), String.valueOf(dontShowLoggerInfo));
        defaultSetting.setProperty(PathHelper.nameToId("SET_EXTRA_LEVEL_BOSS"), String.valueOf(setExtraLevelBoss));
        defaultSetting.setProperty(PathHelper.nameToId("SET_EXTRA_LEVEL_BOSS_TO"), String.valueOf(setExtraLevelBossTo));

        defaultSetting.setProperty(PathHelper.nameToId("TUTORIAL_CLOSED_1"), String.valueOf(tutorialClosed1));
        // 默认值 end

        try {
            config = new SpireConfig("InesModArknights", "Common", defaultSetting);
            config.load();

            // 从config加载
            banExtraLevel = config.getBool(PathHelper.nameToId("BAN_EXTRA_LEVEL"));
            dontShowLoggerInfo = config.getBool(PathHelper.nameToId("DONT_SHOW_LOGGER_INFO"));
            setExtraLevelBoss = config.getBool(PathHelper.nameToId("SET_EXTRA_LEVEL_BOSS"));
            setExtraLevelBossTo = config.getInt(PathHelper.nameToId("SET_EXTRA_LEVEL_BOSS_TO"));

            tutorialClosed1 = config.getBool(PathHelper.nameToId("TUTORIAL_CLOSED_1"));

            LogHelper.info("===加载config: banExtraEnding:{}===", banExtraLevel);
            LogHelper.info("===加载config: tutorialClosed1:{}===",tutorialClosed1);

            // 从config加载 end

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
        ModLabeledToggleButton btn1 = new ModLabeledToggleButton(uis.TEXT[1], 350.0F, 800.0F, Settings.CREAM_COLOR, FontHelper.charDescFont,
                banExtraLevel,
                settingsPanel, modLabel -> {
        }, modToggleButton -> {
            banExtraLevel = modToggleButton.enabled; // 获取按钮的勾选状态
            config.setBool(PathHelper.nameToId("BAN_EXTRA_LEVEL"), banExtraLevel);
            try {
                config.save();
            } catch (IOException e) {
                LogHelper.info("===save config credit failed{}===",e.getLocalizedMessage());
            }
        });
        settingsPanel.addUIElement(btn1);

        // 设置2按钮
        if(!FORCE_ENABLE_INFO){
            ModLabeledToggleButton btn2 = new ModLabeledToggleButton(uis.TEXT[2], 350.0F, 300.0F, Settings.CREAM_COLOR, FontHelper.charDescFont,
                    dontShowLoggerInfo,
                    settingsPanel, modLabel -> {
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
