package InesMod.modcore;

import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;

import InesMod.helpers.ModConfig;
import InesMod.relics.UnassumingNeedle;
import basemod.AutoAdd;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import basemod.BaseMod;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;

import static com.megacrit.cardcrawl.core.Settings.language;


@SpireInitializer
public class InesModMain implements
        EditCharactersSubscriber,
        EditStringsSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditKeywordsSubscriber,
        //OnStartBattleSubscriber,
        PostInitializeSubscriber,
        AddAudioSubscriber

{
    public static final Logger logger = LogManager.getLogger(InesModMain.class);

    // 人物选择界面按钮的图片
    private static final String MY_CHARACTER_BUTTON = "InesModResources/img/char/Character_Button.png";
    // 人物选择界面的立绘
    private static final String MY_CHARACTER_PORTRAIT = "InesModResources/img/char/Character_Portrait.png";
    // 攻击牌的背景（小尺寸）
    private static final String BG_ATTACK_512 = "InesModResources/img/512/bg_attack_512.png";
    // 能力牌的背景（小尺寸）
    private static final String BG_POWER_512 = "InesModResources/img/512/bg_power_512.png";
    // 技能牌的背景（小尺寸）
    private static final String BG_SKILL_512 = "InesModResources/img/512/bg_skill_512.png";
    // 在卡牌和遗物描述中的能量图标
    private static final String SMALL_ORB = "InesModResources/img/char/small_orb.png";
    // 攻击牌的背景（大尺寸）
    private static final String BG_ATTACK_1024 = "InesModResources/img/1024/bg_attack.png";
    // 能力牌的背景（大尺寸）
    private static final String BG_POWER_1024 = "InesModResources/img/1024/bg_power.png";
    // 技能牌的背景（大尺寸）
    private static final String BG_SKILL_1024 = "InesModResources/img/1024/bg_skill.png";
    // 在卡牌预览界面的能量图标
    private static final String BIG_ORB = "InesModResources/img/char/card_orb.png";
    // 小尺寸的能量图标（战斗中，牌堆预览）
    private static final String ENERGY_ORB = "InesModResources/img/char/cost_orb.png";
    // 主题色(116,48,50)
    public static final Color MY_COLOR = new Color(116F / 255.0F, 48F / 255.0F, 50F / 255.0F, 1.0F);

    public static final Color MY_COLOR_DARK = new Color(97F / 255.0F, 41F / 255.0F, 43F / 255.0F, 1.0F);


    public InesModMain() {
        BaseMod.subscribe(this);
        BaseMod.addColor(Ines.Enums.INES_CARD, MY_COLOR, MY_COLOR, MY_COLOR,
                MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR,
                BG_ATTACK_512, BG_SKILL_512, BG_POWER_512, ENERGY_ORB, BG_ATTACK_1024,
                BG_SKILL_1024, BG_POWER_1024, BIG_ORB, SMALL_ORB
        );
        InesModMain.logger.info("===正在回忆设置项===");
        ModConfig.initModSettings();
        InesModMain.logger.info("===设置情报已收集===");
    }


    public static void initialize() {
        new InesModMain();
    }



    @Override
    public void receiveEditCharacters() {
        InesModMain.logger.info("===正在回忆人物===");
        // 向basemod注册人物
        BaseMod.addCharacter(new Ines(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, Ines.Enums.INES);
        InesModMain.logger.info("===人物情报已收集===");
    }

    @Override
    public void receiveEditCards() {
        InesModMain.logger.info("===正在回忆卡牌===");
        // 向basemod注册卡牌
        AutoAdd cards = new AutoAdd("InesModArknights");
        cards.packageFilter(AbstractInesCard.class).setDefaultSeen(false).any(AbstractInesCard.class, (info, card) -> {
            if (card != null) {
                BaseMod.addCard(card);
                if (info.seen) {
                    UnlockTracker.unlockCard(card.cardID);
                }
                UnlockTracker.unlockCard(card.cardID); // TODO：暂时全解锁
            }
        });
        InesModMain.logger.info("===卡牌情报已收集===");
    }



    @Override
    public void receiveEditStrings() {
        String lang = selectLanguage();

        // 加载卡牌文本
        BaseMod.loadCustomStringsFile(CardStrings.class, "InesModResources/localization/" + lang + "/cards.json");
        // 加载角色文本
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "InesModResources/localization/" + lang + "/characters.json");
        // 添加遗物文本
        BaseMod.loadCustomStringsFile(RelicStrings.class, "InesModResources/localization/" + lang + "/relics.json");
        // 添加能力文本
        BaseMod.loadCustomStringsFile(PowerStrings.class, "InesModResources/localization/" + lang + "/powers.json");
        // 添加UI文本
        BaseMod.loadCustomStringsFile(UIStrings.class, "InesModResources/localization/" + lang + "/ui.json");

    }




    @Override
    public void receiveAddAudio() {
        InesModMain.logger.info("===正在回忆音频===");
        // 注册音频
        BaseMod.addAudio("Ines_choose_1", "InesModResources/sound/Ines_choose_1.wav");
        BaseMod.addAudio("Ines_choose_2", "InesModResources/sound/Ines_choose_2.wav");

        InesModMain.logger.info("===音频情报已收集===");
    }


    @Override
    public void receiveEditRelics() {
        InesModMain.logger.info("===正在回忆遗物===");
        // 注册遗物
        BaseMod.addRelic(new UnassumingNeedle(), RelicType.SHARED);

        InesModMain.logger.info("===遗物情报已收集===");
    }

    @Override
    public void receiveEditKeywords() {
        InesModMain.logger.info("===正在回忆关键词===");
        String lang = selectLanguage();

        Gson gson = new Gson();
        String json = Gdx.files.internal("InesModResources/localization/" + lang + "/keywords.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                // 这个id要全小写
                BaseMod.addKeyword("ines", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }

        InesModMain.logger.info("===关键词情报已收集===");
    }

    @Override
    public void receivePostInitialize() {
        ModConfig.initModConfigMenu();
    }


    // 辅助方法
    private String selectLanguage(){
        String lang;
        if (language == Settings.GameLanguage.ZHS) {
            lang = "ZHS"; // 如果语言设置为简体中文，则加载ZHS文件夹的资源
        } else {
            lang = "ENG"; // 如果没有相应语言的版本，默认加载英语
        }
        return lang;
    }
}