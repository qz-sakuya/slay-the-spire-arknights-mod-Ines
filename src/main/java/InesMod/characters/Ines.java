package InesMod.characters;


// 省略package路径和部分import，复制的时候不要忘记写上自己的package

import InesMod.cards.attack.ShadowAmbush;
import InesMod.cards.attack.Strike;
import InesMod.cards.skill.Defend;
import InesMod.cards.skill.EdgeOfLight;
import InesMod.cards.skill.PlanOfAction;
import InesMod.modcore.InesModMain;
import InesMod.relics.UnassumingNeedle;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.Vajra;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import java.util.ArrayList;


import static sun.misc.Version.print;

public class Ines extends CustomPlayer {
    // 火堆的人物立绘（行动前）
    private static final String MY_CHARACTER_SHOULDER_1 = "InesModResources/img/char/shoulder1.png";
    // 火堆的人物立绘（行动后）
    private static final String MY_CHARACTER_SHOULDER_2 = "InesModResources/img/char/shoulder2.png";
    // 人物死亡图像
    private static final String CORPSE_IMAGE = "InesModResources/img/char/corpse.png";
    // 战斗界面左下角能量图标的每个图层
    private static final String[] ORB_TEXTURES = new String[]{
            "InesModResources/img/UI/orb/layer5.png",
            "InesModResources/img/UI/orb/layer4.png",
            "InesModResources/img/UI/orb/layer3.png",
            "InesModResources/img/UI/orb/layer2.png",
            "InesModResources/img/UI/orb/layer1.png",
            "InesModResources/img/UI/orb/layer6.png",
            "InesModResources/img/UI/orb/layer5d.png",
            "InesModResources/img/UI/orb/layer4d.png",
            "InesModResources/img/UI/orb/layer3d.png",
            "InesModResources/img/UI/orb/layer2d.png",
            "InesModResources/img/UI/orb/layer1d.png"
    };
    // 每个图层的旋转速度
    private static final float[] LAYER_SPEED = new float[]{-40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F};
    // 人物的本地化文本，如卡牌的本地化文本一样，如何书写见下
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("InesMod:Ines");

    public Ines(String name) {
        super(name, Enums.INES, ORB_TEXTURES,"InesModResources/img/UI/orb/vfx.png", LAYER_SPEED, null, null);


        // 人物对话气泡的大小，如果游戏中尺寸不对在这里修改（libgdx的坐标轴左下为原点）
        this.dialogX = (this.drawX + 0.0F * Settings.scale);
        this.dialogY = (this.drawY + 150.0F * Settings.scale);


        // 初始化你的人物，如果你的人物只有一张图，那么第一个参数填写你人物图片的路径。
        this.initializeClass(
                null,  // 人物图片
                MY_CHARACTER_SHOULDER_2, MY_CHARACTER_SHOULDER_1,
                CORPSE_IMAGE, // 人物死亡图像
                this.getLoadout(),
                0.0F, 0.0F,
                200.0F, 220.0F, // 人物碰撞箱大小，越大的人物模型这个越大
                new EnergyManager(3) // 初始每回合的能量
        );

        // 设置动画
        InesModMain.logger.info("===============开始导入模型================");
        loadAnimation("InesModResources/model/char_4087_ines.atlas",
                "InesModResources/model/char_4087_ines.json",
                        1.8F);

        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        e.setTimeScale(1.2F);


    }

    // 初始卡组的ID，可直接写或引用变量
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> cardList = new ArrayList<>();


//        for(int x = 0; x<5; x++) {
//            cardList.add(Strike.ID);
//        }
//        for(int x = 0; x<5; x++) {
//            cardList.add(Defend.ID);
//        }
//        cardList.add(PlanOfAction.ID);

        // TODO：调试用卡组
        for(int x = 0; x<1; x++) {
            cardList.add(Strike.ID);
            cardList.add(Defend.ID);
            cardList.add(PlanOfAction.ID);
            cardList.add(ShadowAmbush.ID);
            cardList.add(EdgeOfLight.ID);
        }


        return cardList;
    }

    // 初始遗物的ID，可以先写个原版遗物凑数
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> relicList = new ArrayList<>();
        relicList.add(UnassumingNeedle.ID);
        return relicList;
    }

    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(
                characterStrings.NAMES[0], // 人物名字
                characterStrings.TEXT[0], // 人物介绍
                70, // 当前血量
                70, // 最大血量
                0, // 初始充能球栏位
                99, // 初始携带金币
                5, // 每回合抽牌数量
                this, // 别动
                this.getStartingRelics(), // 初始遗物
                this.getStartingDeck(), // 初始卡组
                false // 别动
        );
    }

    @Override
    public void useFastAttackAnimation() {
        this.state.setAnimation(0, "Attack", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.state.getCurrent(0).setTimeScale(1.2F);
    }

    @Override
    public void useSlowAttackAnimation() {
        this.state.setAnimation(0, "Skill_2_Begin", false);
        this.state.setAnimation(0, "Skill_2_Attack", false);
        this.state.setAnimation(0, "Skill_2_End", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.state.getCurrent(0).setTimeScale(1.2F); // 加快动画速度
    }

    @Override
    public void playDeathAnimation() {
        this.state.setAnimation(0, "Die", false);
    }

    // 人物名字（出现在游戏左上角）
    @Override
    public String getTitle(PlayerClass playerClass) {
        return characterStrings.NAMES[0];
    }

    // 你的卡牌颜色（这个枚举在最下方创建）
    @Override
    public AbstractCard.CardColor getCardColor() {
        return Enums.INES_CARD;
    }

    // 翻牌事件出现的你的职业牌（一般设为打击）
    @Override
    public AbstractCard getStartCardForEvent() {
        return new Strike();
    }

    // 卡牌轨迹颜色
    @Override
    public Color getCardTrailColor() {
        return InesModMain.MY_COLOR;
    }

    // 高进阶带来的生命值损失
    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    // 卡牌的能量字体，没必要修改
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    // 人物选择界面点击你的人物按钮时触发的方法，这里为屏幕轻微震动
    @Override
    public void doCharSelectScreenSelectEffect() {
        // InesModMain.logger.info("===点击人物选择按钮，开始播放音频===");
        if (MathUtils.randomBoolean()) {
            CardCrawlGame.sound.playV("Ines_choose_1", 1.3F);
        } else {
            CardCrawlGame.sound.playV("Ines_choose_2", 1.2F);
        }

        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    // 碎心图片
    @Override
    public ArrayList<CutscenePanel> getCutscenePanels() {
        ArrayList<CutscenePanel> panels = new ArrayList<>();
        // 有两个参数的，第二个参数表示出现图片时播放的音效
        panels.add(new CutscenePanel("InesModResources/img/char/Victory1.png", "ATTACK_MAGIC_FAST_1"));
        panels.add(new CutscenePanel("InesModResources/img/char/Victory2.png"));
        panels.add(new CutscenePanel("InesModResources/img/char/Victory3.png"));
        return panels;
    }

    // 自定义模式选择你的人物时播放的音效
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_HEAVY";
    }

    // 游戏中左上角显示在你的名字之后的人物名称
    @Override
    public String getLocalizedCharacterName() {
        return characterStrings.NAMES[0];
    }

    // 创建人物实例，照抄
    @Override
    public AbstractPlayer newInstance() {
        return new Ines(this.name);
    }

    // 第三章面对心脏说的话（例如战士是“你握紧了你的长刀……”之类的）
    @Override
    public String getSpireHeartText() {
        return characterStrings.TEXT[1];
    }

    // 打心脏的颜色，不是很明显
    @Override
    public Color getSlashAttackColor() {
        return InesModMain.MY_COLOR;
    }

    // 吸血鬼事件文本，主要是他（索引为0）和她（索引为1）的区别（机器人另外）
    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[0];
    }

    // 卡牌选择界面选择该牌的颜色
    @Override
    public Color getCardRenderColor() {
        return InesModMain.MY_COLOR;
    }

    // 第三章面对心脏造成伤害时的特效
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL};
    }




    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass INES;
        @SpireEnum(name = "INES_DARKRED")
        public static AbstractCard.CardColor INES_CARD;
        @SpireEnum(name = "INES_DARKRED")
        public static CardLibrary.LibraryType INES_LIBRARY;
        @SpireEnum
        public static AbstractCard.CardTags PACKAGE;
    }


}
