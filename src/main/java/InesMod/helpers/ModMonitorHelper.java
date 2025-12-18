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

public class ModMonitorHelper {
    /*
    在未来编写多个mod时，为避免不同mod中相同功能的patch（或核心类监听接口，待测试）因为同时加载了多个mod而意外多次触发，
    构思了这个mod监视器类

    其作用是，在启动时从启动器获取所有被启动的mod，如果其中有需要监视的mod，则记录下来

    每个希望监视的mod都有固定的 monitorPriority ，这个是在自己的所有mod中写死的，
    同时包括一个 modid - monitorPriority 的词典（也是写死的）

    对于每个mod中可能的重复部分（如重复的patch），在其运行前，判断比其优先级更高的，且希望监视的mod是否存在， 如果存在则不运行
    这样保证了无论怎么加载mod，都只有优先级最高的一个mod会运行重复部分

    ModMonitorHelper 会提供一个核心函数：Monitor(int Priority1,...)
    该函数的参数表示，所有希望监视的mod（的标识）。希望监视的mod的优先级，必须大于当前mod的优先级

    不同patch可以使用不同的Monitor参数，例如patchA只在两个mod中重复，而patchB在所有mod中重复



    当然，由于我目前只有写了一个mod，所以该类暂不实现
  */
}
