package InesMod.helpers;

import InesMod.modcore.InesModMain;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.FtueTip;
import org.apache.commons.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;

public class LoggerHelper {
    private static final Logger LOGGER = LogManager.getLogger(LoggerHelper.class);
    public static boolean FORCE_ENABLE_INFO = true; // 强制启用日志，仅用于开发环境 // TODO：记得关

    public static void info(String message, Object... params) {
        if (FORCE_ENABLE_INFO || !ConfigHelper.dontShowLoggerInfo) {
            LOGGER.info(message, params);
        }
    }
}
