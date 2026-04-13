package InesMod.patchs;

import InesMod.dungeons.LevelChapter10;
import InesMod.helpers.InesExtraLevelHelper;
import InesMod.helpers.LogHelper;
import InesMod.relics.ShadowOfLondinium;
import InesMod.room.ExtraLevelTreasureRoom;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import com.megacrit.cardcrawl.rooms.TrueVictoryRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
/*     */ import com.badlogic.gdx.audio.Music;

import java.util.Objects;

public class ExtraLevelPatch  {

    public static boolean EnterChapter10 = false;
    public static boolean EnteredChapter10 = false;

    public static boolean EnterChapter11 = false;
    public static boolean EnteredChapter11 = false;

    public static boolean EnterChapter12 = false;
    public static boolean EnteredChapter12 = false;

    public static boolean EnterChapter13 = false;
    public static boolean EnteredChapter13 = false;

    public static String BOSS_MUSIC_KEY = "";

    // 获取地牢实例
    @SpirePatch(clz = CardCrawlGame.class,method = "getDungeon",paramtypez = {String.class, AbstractPlayer.class})
    public static class GetDungeonPatch{
        @SpirePostfixPatch
        public static AbstractDungeon Postfix(AbstractDungeon _ret, CardCrawlGame _inst, String key, AbstractPlayer p){

            if(key.equals(LevelChapter10.ID)){
                return new LevelChapter10(p,AbstractDungeon.specialOneTimeEventList);
            }


            return _ret;
        }
    }

    // 获取地牢实例
    @SpirePatch(clz = CardCrawlGame.class,method = "getDungeon",paramtypez = {String.class,AbstractPlayer.class, SaveFile.class})
    public static class GetDungeonOnSavePatch{
        @SpirePostfixPatch
        public static AbstractDungeon Postfix(AbstractDungeon _ret, CardCrawlGame _inst, String key, AbstractPlayer p,SaveFile file){

            if(key.equals(LevelChapter10.ID)){
                return new LevelChapter10(p,file);
            }

            return _ret;
        }
    }





    @SpirePatch(clz = DungeonMap.class,method = "render")
    public static class RenderPatch{
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(DungeonMap _inst, SpriteBatch sb){
            // 加入对本mod地牢的支持
            if(InesExtraLevelHelper.isInesExtraLevelID(AbstractDungeon.id)){
                ReflectionHacks.privateMethod(DungeonMap.class,"renderFinalActMap", SpriteBatch.class).invoke(_inst, sb);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = DungeonMap.class,method = "renderMapBlender")
    public static class RenderBlenderPatch{
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(DungeonMap _inst, SpriteBatch sb){
            // 加入对本mod地牢的支持
            if(InesExtraLevelHelper.isInesExtraLevelID(AbstractDungeon.id))
                return SpireReturn.Return();
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = DungeonMapScreen.class,method = "open")
    public static class OpenMapPatch{
        @SpireInsertPatch(rloc = 8)
        public static void Insert(DungeonMapScreen _inst,boolean doScrollingAnimation){
            // Chapter10
            if(AbstractDungeon.id.equals(LevelChapter10.ID)){
                // 自定义尺寸
                // 这个值越小，地图界面中可向上滑动的范围越大
                // 负值大概是因为要高于地图
                ReflectionHacks.setPrivate(_inst,DungeonMapScreen.class,"mapScrollUpperLimit",-1000.0F * Settings.scale);
            }
        }
    }

    @SpirePatch(clz = DungeonMap.class,method = "calculateMapSize")
    public static class MapSizePatch{
        @SpirePostfixPatch
        public static float Postfix(float _ret,DungeonMap _inst){
            if(AbstractDungeon.id.equals(LevelChapter10.ID)) {
                // 自定义尺寸
                // 这个结果越大，地图卷轴越长
                float size = Settings.MAP_DST_Y * 4.0F - 600.0F * Settings.scale;
                return size;
            }
            return _ret;
        }
    }




    // Boss前一个房间 连接 Boss房间
    @SpirePatch(clz = DungeonMap.class, method = "update")
    public static class UpdatePatch {
        @SpireInsertPatch(rloc = 40)
        public static void Insert(DungeonMap _inst) {
            if (_inst.bossHb.hovered && (InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed())) {
                if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMPLETE && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) {
                    if (Settings.isDebug) {
                        handleBossTransition(_inst);
                    }

                    // Chapter10
                    else if (AbstractDungeon.id.equals(LevelChapter10.ID) && AbstractDungeon.getCurrMapNode().y == 7) {// y为boss前一个房间
                        handleBossTransition(_inst);
                    }
                }
            }
        }

        private static void handleBossTransition(DungeonMap _inst) {
            AbstractDungeon.getCurrMapNode().taken = true;
            MapRoomNode node2 = AbstractDungeon.getCurrMapNode();
            for (MapEdge e : node2.getEdges()) {
                if (e != null) {
                    e.markAsTaken();
                }
            }

            InputHelper.justClickedLeft = false;
            CardCrawlGame.music.fadeOutTempBGM();

            MapRoomNode node = new MapRoomNode(-1, 15);
            node.room = new MonsterRoomBoss();
            AbstractDungeon.nextRoom = node;

            // pathX 存储走过的房间的 X 坐标，pathY 存储对应的 Y 坐标。
            if (AbstractDungeon.pathY.size() > 1) {
                // X不变，Y+1
                AbstractDungeon.pathX.add(AbstractDungeon.pathX.get(AbstractDungeon.pathX.size() - 1));
                AbstractDungeon.pathY.add(AbstractDungeon.pathY.get(AbstractDungeon.pathY.size() - 1) + 1);
            } else {
                // 缺省值
                AbstractDungeon.pathX.add(1);
                AbstractDungeon.pathY.add(15);
            }

            AbstractDungeon.nextRoomTransitionStart();
            _inst.bossHb.hovered = false;
        }
    }

    // Boss房间 连接 TrueVictory房间
    @SpirePatch(clz = ProceedButton.class,method = "update")
    public static class EndExtraLevelPatch {
        @SpireInsertPatch(rloc = 40)
        public static void Insert(ProceedButton _inst){
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
                if (InesExtraLevelHelper.isInesExtraLevelID(AbstractDungeon.id)) {
                    CardCrawlGame.music.fadeOutBGM();

                    MapRoomNode node = new MapRoomNode(-1, 16); // 没研究过坐标是否必须对应
                    node.room = new TrueVictoryRoom();

                    AbstractDungeon.nextRoom = node;
                    AbstractDungeon.closeCurrentScreen();

                    AbstractDungeon.nextRoomTransitionStart();

                    _inst.hide();
                }
            }
        }
    }

    // 进入TrueVictory房间时，判断是否进入（下一个）额外层级
    @SpirePatch(clz = TrueVictoryRoom.class,method = "onPlayerEntry")
    public static class TrueVictoryPatch{
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(TrueVictoryRoom _inst){
            LogHelper.info("===TrueVictoryRoom：begin===");
            LogHelper.info("===TrueVictoryRoom：当前EnteredChapter10={}，EnterChapter10={}===",EnteredChapter10,EnterChapter10);

            boolean continueIt = false;

            // 检查是否有结局遗物
            for(AbstractRelic r : AbstractDungeon.player.relics){
                if(r.relicId.equals(ShadowOfLondinium.ID)){
                    ShadowOfLondinium shadowOfLondinium = (ShadowOfLondinium)r;
                    if (shadowOfLondinium.banExtraLevel){
                        break;
                    }

                    int levelType = shadowOfLondinium.getNextLevel();

                    if(levelType == 0 && !EnteredChapter10 && !EnterChapter10){
                        EnterChapter10 = true;
                        continueIt = true;
                        break;
                    }
                }
            }


            LogHelper.info("===TrueVictoryRoom：判断后：EnteredChapter10={}，EnterChapter10={}===",EnteredChapter10,EnterChapter10);



            // 进入额外层级前，补充一个BOSS宝箱房
            if(continueIt){
                LogHelper.info("===TrueVictoryRoom：补充BOSS宝箱房===");

                CardCrawlGame.stopClock = false;
                CardCrawlGame.music.fadeOutTempBGM();

                MapRoomNode node = new MapRoomNode(-1, 15);
                node.room = new ExtraLevelTreasureRoom();
                AbstractDungeon.nextRoom = node;

                AbstractDungeon.closeCurrentScreen();

                LogHelper.info("===TrueVictoryRoom：即将进入补充的BOSS宝箱房===");
                CardCrawlGame.dungeon.nextRoomTransition();

                return SpireReturn.Return(); // 强制退出，打断TrueVictory的结算逻辑
            }

            return SpireReturn.Continue();
        }
    }

    //BOSS宝箱房修正
    @SpirePatch(clz = AbstractDungeon.class,method = "populatePathTaken")
    public static class PopulatePathTakenPatch{
        @SpireInsertPatch(rloc = 25,localvars = {"node"})
        public static void Insert(AbstractDungeon _inst,SaveFile saveFile, MapRoomNode node){
            if (saveFile.current_room.equals(ExtraLevelTreasureRoom.class.getName())) {
                node = new MapRoomNode(-1, 15);
                node.room = new ExtraLevelTreasureRoom();
                AbstractDungeon.nextRoom = node;
            }
        }
    }

    // 标记是否进入一个额外层级
    // 仅当进入过BOSS房，才标记已经进入过
    @SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition",paramtypez = {SaveFile.class})
    public static class EnteredPatch{
        @SpirePostfixPatch
        public static void Postfix(AbstractDungeon _inst, SaveFile saveFile){

            // Chapter10
            if(CardCrawlGame.dungeon instanceof LevelChapter10 && AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss){
                EnteredChapter10 = true;
            }
        }
    }

    @SpirePatch(clz = TreasureRoomBoss.class,method = "getNextDungeonName")
    public static class TreasureBossPatch{
        @SpirePostfixPatch
        public static String Postfix(String _ret,TreasureRoomBoss _inst){
            LogHelper.info("===TreasureBossPatch：begin===");
            LogHelper.info("===TreasureBossPatch：当前EnteredChapter10={}，EnterChapter10={}===",EnteredChapter10,EnterChapter10);


            // Chapter10
            if(!EnteredChapter10 && EnterChapter10){
                LogHelper.info("===TreasureBossPatch：触发：Chapter10===");
                return LevelChapter10.ID;
            }
            return _ret;
        }
    }

    @SpirePatch(clz = ProceedButton.class,method = "goToNextDungeon")
    public static class ProceedButtonPatch {
        @SpirePostfixPatch
        public static void Postfix(ProceedButton _inst){
            LogHelper.info("===ProceedButtonPatch：begin===");
            LogHelper.info("===ProceedButtonPatch：当前EnteredChapter10={}，EnterChapter10={}===",EnteredChapter10,EnterChapter10);

            // Chapter10
            if(!EnteredChapter10 && EnterChapter10){
                LogHelper.info("===ProceedButtonPatch：触发：Chapter10===");
                CardCrawlGame.nextDungeon = LevelChapter10.ID;
            }
        }
    }

    @SpirePatch(clz = MainMusic.class, method = "getSong")
    public static class MainMusicPatch
    {
        @SpirePostfixPatch
        public static Music Postfix(Music _result, MainMusic _inst, String key) {
            if (key.equals(LevelChapter10.ID)) {
                return MainMusic.newMusic("InesModResources/audio/dungeon/chapter10/C10_Main.mp3");
            }
            if (key.equals("C10_B1_MFLD")) {
                return MainMusic.newMusic("InesModResources/audio/dungeon/chapter10/C10_Boss_1.mp3");
            }

            return _result;
        }
    }



    @SpirePatch(clz = TempMusic.class, method = "getSong")
    public static class TempMusicPatch
    {
        @SpirePostfixPatch
        public static Music Postfix(Music _result, TempMusic _inst, String key) {
            // 注意：因为原版限制，只能使用"BOSS_BEYOND"这个key，然后再修改
            if (key.equals("BOSS_BEYOND") ) {
                if (Objects.equals(BOSS_MUSIC_KEY, "C10_Boss_1")){
                    return MainMusic.newMusic("InesModResources/audio/dungeon/chapter10/C10_Boss_1.mp3");
                }
                if (Objects.equals(BOSS_MUSIC_KEY, "C10_Boss_2")){
                    return MainMusic.newMusic("InesModResources/audio/dungeon/chapter10/C10_Boss_2.mp3");
                }

            }
            BOSS_MUSIC_KEY = "";
            return _result;
        }
    }
}
