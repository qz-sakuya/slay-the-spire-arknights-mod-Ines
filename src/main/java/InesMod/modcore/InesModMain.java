package InesMod.modcore;

import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;

import InesMod.enums.InesCardTags;
import InesMod.helpers.ConfigHelper;
import InesMod.helpers.LogHelper;
import InesMod.helpers.MonsterHelper;
import InesMod.helpers.PathHelper;
import InesMod.relics.RustedNeedle;
import InesMod.truth.TruthManager;
import InesMod.truth.TruthReward;
import InesMod.enums.OtherEnum;
import InesMod.relics.UnassumingNeedle;
import basemod.AutoAdd;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import basemod.BaseMod;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.*;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static InesMod.characters.Ines.Enums.INES_CARD;
import static com.megacrit.cardcrawl.core.Settings.language;


@SpireInitializer
public class InesModMain implements
        EditCharactersSubscriber,
        EditStringsSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditKeywordsSubscriber,
        PostInitializeSubscriber,
        AddAudioSubscriber,
        PostDrawSubscriber,
        OnCardUseSubscriber,
        PostExhaustSubscriber,
        OnStartBattleSubscriber,
//        PostBattleSubscriber,
//        PostCampfireSubscriber,
        PostPlayerUpdateSubscriber,
        StartGameSubscriber,
        PreStartGameSubscriber,
        OnPlayerTurnStartSubscriber

{
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
    // 主题色：暗红色(116,48,50)
    public static final Color MY_COLOR = new Color(116F / 255.0F, 48F / 255.0F, 50F / 255.0F, 1.0F);

    public static final Color MY_COLOR_DARK = new Color(97F / 255.0F, 41F / 255.0F, 43F / 255.0F, 1.0F);

    private static UIStrings retainThisTurnStrings = null;

    public InesModMain() {
        BaseMod.subscribe(this);
        BaseMod.addColor(
                INES_CARD,          // color: 卡牌颜色ID (AbstractCard.CardColor 枚举值)
                MY_COLOR,           // bgColor: 卡牌背景主色调
                MY_COLOR,           // backColor: 卡牌背面颜色
                MY_COLOR,           // frameColor: 卡牌边框颜色
                MY_COLOR,           // frameOutlineColor: 卡牌边框描边/轮廓颜色
                MY_COLOR,           // descBoxColor: 卡牌描述文本框的背景颜色
                MY_COLOR,           // trailVfxColor: 抽牌/弃牌时的拖尾特效颜色
                MY_COLOR,           // glowColor: 卡牌高亮/发光颜色
                BG_ATTACK_512,      // attackBg: 攻击卡背景贴图路径 (512x512 分辨率)
                BG_SKILL_512,       // skillBg: 技能卡背景贴图路径 (512x512 分辨率)
                BG_POWER_512,       // powerBg: 能力卡背景贴图路径 (512x512 分辨率)
                ENERGY_ORB,         // energyOrb: 小尺寸的能量图标（战斗中，牌堆预览）
                BG_ATTACK_1024,     // attackBgPortrait: 攻击卡背景贴图路径 (1024x1024 高分辨率)
                BG_SKILL_1024,      // skillBgPortrait: 技能卡背景贴图路径 (1024x1024 高分辨率)
                BG_POWER_1024,      // powerBgPortrait: 能力卡背景贴图路径 (1024x1024 高分辨率)
                BIG_ORB,            // energyOrbPortrait: 在卡牌预览界面的能量图标
                SMALL_ORB           // cardEnergyOrb: 在卡牌和遗物描述中的能量图标
        );
        LogHelper.info("===正在回忆设置项===");
        ConfigHelper.initModSettings();
        LogHelper.info("===设置情报已收集===");
    }


    public static void initialize() {
        new InesModMain();
    }



    @Override
    public void receiveEditCharacters() {
        LogHelper.info("===正在回忆人物===");
        // 向basemod注册人物
        BaseMod.addCharacter(new Ines(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, Ines.Enums.INES);
        LogHelper.info("===人物情报已收集===");
    }

    @Override
    public void receiveEditCards() {
        LogHelper.info("===正在回忆卡牌===");
        // 向basemod注册卡牌
        AutoAdd cards = new AutoAdd("InesModArknights");
        cards.packageFilter(AbstractInesCard.class).setDefaultSeen(false).any(AbstractInesCard.class, (info, card) -> {
            if (card != null) {
                BaseMod.addCard(card);
                if (info.seen) {
                    UnlockTracker.unlockCard(card.cardID);
                }
                UnlockTracker.unlockCard(card.cardID); // 暂时全解锁
            }
        });
        LogHelper.info("===卡牌情报已收集===");
    }



    @Override
    public void receiveEditStrings() {
        String lang = selectLanguage();

        // 加载卡牌文本
        BaseMod.loadCustomStringsFile(CardStrings.class, "InesModResources/localization/" + lang + "/cards.json");
        // 加载角色文本
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "InesModResources/localization/" + lang + "/characters.json");
        // 加载怪物文本
        BaseMod.loadCustomStringsFile(MonsterStrings.class, "InesModResources/localization/" + lang + "/monsters.json");
        // 加载遗物文本
        BaseMod.loadCustomStringsFile(RelicStrings.class, "InesModResources/localization/" + lang + "/relics.json");
        // 加载能力文本
        BaseMod.loadCustomStringsFile(PowerStrings.class, "InesModResources/localization/" + lang + "/powers.json");
        // 加载UI文本
        BaseMod.loadCustomStringsFile(UIStrings.class, "InesModResources/localization/" + lang + "/ui.json");
    }




    @Override
    public void receiveAddAudio() {
        LogHelper.info("===正在回忆音频===");
        // 注册音频
        BaseMod.addAudio("Ines_choose_1", "InesModResources/sound/Ines_choose_1.wav");
        BaseMod.addAudio("Ines_choose_2", "InesModResources/sound/Ines_choose_2.wav");

        LogHelper.info("===音频情报已收集===");
    }


    @Override
    public void receiveEditRelics() {
        LogHelper.info("===正在回忆遗物===");
        // 注册遗物
        // BaseMod.addRelic(new UnassumingNeedle(), RelicType.SHARED);
        BaseMod.addRelicToCustomPool(new UnassumingNeedle(), INES_CARD);
        BaseMod.addRelicToCustomPool(new RustedNeedle(), INES_CARD);


        LogHelper.info("===遗物情报已收集===");
    }

    @Override
    public void receiveEditKeywords() {
        LogHelper.info("===正在回忆关键词===");
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

        LogHelper.info("===关键词情报已收集===");
    }

    @Override
    public void receivePostInitialize() {
        MonsterHelper.initializeMonsters();
        ConfigHelper.initModConfigMenu();

        BaseMod.registerCustomReward(OtherEnum.INES_TRUTH,
                rewardSave -> new TruthReward(rewardSave.amount, false),
                customReward -> new RewardSave(customReward.type.toString(),
                        null,
                        ((TruthReward)customReward).amount, 0));
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        TruthManager.setTopPanelItem();


        // 精英领袖房获得额外真相奖励
        ArrayList<RewardItem> rewards = AbstractDungeon.getCurrRoom().rewards;
        if (AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomElite) {
            int truthFromElite = 2;
            rewards.add(0, new TruthReward(truthFromElite, false));
        }
        else if (AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss) {
            int truthFromBoss = 3;
            rewards.add(0, new TruthReward(truthFromBoss, false));
        }
    }

    @Override
    public void receivePostPlayerUpdate() {}

    @Override
    public void receiveStartGame() {
        TruthManager.setTopPanelItem();
    }



    @Override
    public void receivePreStartGame(){
        // 进入存档时，清空未应用的虚值
        TruthManager.clearVirtual();
    }


    @Override
    public void receivePostDraw(AbstractCard c) {
        // 暂时没用到
    }

    @Override
    public void receivePostExhaust(AbstractCard c){
        // 弃用此接口，改为自定义回调
    }

    @Override
    public void  receiveOnPlayerTurnStart(){
        LogHelper.info("===InesModMain: receiveOnPlayerTurnStart===");

        // 重置“在本回合保留。”词条
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            resetCardsRetainThisTurn(c);
        }

        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            resetCardsRetainThisTurn(c);
        }

        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            resetCardsRetainThisTurn(c);
        }

        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            resetCardsRetainThisTurn(c);
        }
    }

    @Override
    public void receiveCardUsed(AbstractCard c) {
        LogHelper.info("===InesModMain: receiveCardUsed===");

        for (AbstractCard cardToCall : AbstractDungeon.player.hand.group) {
            if (cardToCall instanceof AbstractInesCard){
                AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                inesCard.onReceiveCardUsed(c);
            }
        }

        for (AbstractCard cardToCall : AbstractDungeon.player.discardPile.group) {
            if (cardToCall instanceof AbstractInesCard){
                AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                inesCard.onReceiveCardUsed(c);
            }
        }

        for (AbstractCard cardToCall : AbstractDungeon.player.drawPile.group) {
            if (cardToCall instanceof AbstractInesCard){
                AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                inesCard.onReceiveCardUsed(c);
            }
        }

        for (AbstractCard cardToCall : AbstractDungeon.player.exhaustPile.group) {
            if (cardToCall instanceof AbstractInesCard){
                AbstractInesCard inesCard = (AbstractInesCard)cardToCall;
                inesCard.onReceiveCardUsed(c);
            }
        }
    }


    // ========辅助方法=========

    private String selectLanguage(){
        String lang;
        if (language == Settings.GameLanguage.ZHS) {
            lang = "ZHS"; // 如果语言设置为简体中文，则加载ZHS文件夹的资源
        } else {
            lang = "ENG"; // 如果没有相应语言的版本，默认加载英语
        }
        return lang;
    }

    // 如果卡牌具有“在本回合保留。”，则重置
    private void resetCardsRetainThisTurn(AbstractCard card) {
        if (retainThisTurnStrings == null) {
            retainThisTurnStrings = CardCrawlGame.languagePack.getUIString(PathHelper.nameToId("RetainCardsThisTurnAction"));
        }

        if (card.tags.contains(InesCardTags.RetainThisTurn)) {
            // 删除tag（文本由patch处理）
            card.tags.remove(InesCardTags.RetainThisTurn);
            card.initializeDescription();

            // 不再保留
            card.retain = false;
        }
    }


}