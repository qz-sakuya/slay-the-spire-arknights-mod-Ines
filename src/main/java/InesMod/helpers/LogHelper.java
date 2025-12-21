package InesMod.helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {
    private static final Logger LOGGER = LogManager.getLogger(LogHelper.class);
    public static boolean FORCE_ENABLE_INFO = true; // 强制启用日志，仅用于开发环境 // TODO：记得关

    public static void info(String message, Object... params) {
        if (FORCE_ENABLE_INFO || !ConfigHelper.dontShowLoggerInfo) {
            LOGGER.info(message, params);
        }
    }
}
