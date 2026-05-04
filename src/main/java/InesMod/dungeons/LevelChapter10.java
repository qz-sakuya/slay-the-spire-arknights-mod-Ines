package InesMod.dungeons;

import InesMod.helpers.DungeonHelper;
import InesMod.helpers.LogHelper;
import InesMod.helpers.MonsterHelper;
import InesMod.helpers.PathHelper;
import InesMod.truth.TruthTopItem;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import com.megacrit.cardcrawl.rooms.TrueVictoryRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;


/**
 * 额外层级
 * 对应主线第10章
 *
 * 小怪：战士、工匠、补给车
 * 精英：凋亡术士（低攻。弹射：轮到自己攻击时，玩家有格挡则额外造成一次伤害）、大刀哥
 * 衍生：造物，大造物
 * boss：曼弗雷德
 *
 *
 *
 * group：
 * Weak：2战士、2小造物+补给车、战士+补给车  （2、3不会连续出现，但因为只生成一个弱怪，不用去重了）
 * Strong：3战士、2战士+补给车、战士+工匠、3小造物+工匠、大造物+工匠   （3、4、5不会连续出现）
 * Elite：2术士、大刀哥+术士、2战士+大刀哥、3小造物+大刀哥  （3、4不会连续出现）
 * Boss：2战士+曼弗雷德，二阶段还有2战士
 */
public class LevelChapter10 extends AbstractDungeon {
    public static final String ID = PathHelper.nameToId(LevelChapter10.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public static final String[] TEXT = uiStrings.TEXT;
    public static final String NAME = TEXT[0];



    public LevelChapter10(AbstractPlayer p, ArrayList<String> theList) {
        super(NAME, ID, p, theList);
        if (scene != null) {
            scene.dispose();
        }

        scene = new LevelChapter10Scene();

        // 颜色默认就行
        fadeColor = Color.valueOf("140a1eff");
        sourceFadeColor = Color.valueOf("140a1eff");

        initializeLevelSpecificChances();
        mapRng = new Random(Settings.seed + (AbstractDungeon.actNum * 400L));
        generateSpecialMap();
        CardCrawlGame.music.changeBGM(id);


    }

    public LevelChapter10(AbstractPlayer p, SaveFile saveFile) {
        super(NAME, p, saveFile);
        CardCrawlGame.dungeon = this;
        if (scene != null) {
            scene.dispose();
        }

        scene = new LevelChapter10Scene();
        fadeColor = Color.valueOf("140a1eff");
        sourceFadeColor = Color.valueOf("140a1eff");
        initializeLevelSpecificChances();
        miscRng = new Random(Long.valueOf(Settings.seed.longValue() + saveFile.floor_num));
        CardCrawlGame.music.changeBGM(id);
        mapRng = new Random(Long.valueOf(Settings.seed.longValue() + (saveFile.act_num * 300)));
        generateSpecialMap();
        firstRoomChosen = true;
        populatePathTaken(saveFile);
    }

    private void generateSpecialMap() {
        MapRoomNode node0 = DungeonHelper.createNode(3, 0, new RestRoom());
        MapRoomNode node1 = DungeonHelper.createNode(3, 1, new ShopRoom());
        MapRoomNode node2 = DungeonHelper.createNode(2, 2, new MonsterRoom());
        MapRoomNode node3 = DungeonHelper.createNode(2, 3, new MonsterRoomElite());
        MapRoomNode node4 = DungeonHelper.createNode(3, 4, new TreasureRoom());
        MapRoomNode node5 = DungeonHelper.createNode(4, 5, new RestRoom());
        MapRoomNode node6 = DungeonHelper.createNode(4, 6, new MonsterRoom());
        MapRoomNode node7 = DungeonHelper.createNode(3, 7, new MonsterRoomElite());
        MapRoomNode node8 = DungeonHelper.createNode(3, 8, new MonsterRoomBoss());
        MapRoomNode node9 = DungeonHelper.createNode(3, 9, new TrueVictoryRoom());



        map = DungeonHelper.createMap(node0,node1,node2,node3,node4,node5,node6,node7,node8,node9);

        DungeonHelper.connectNode(node0, node1);
        DungeonHelper.connectNode(node1, node2);
        DungeonHelper.connectNode(node2, node3);
        DungeonHelper.connectNode(node3, node4);
        DungeonHelper.connectNode(node4, node5);
        DungeonHelper.connectNode(node5, node6);
        DungeonHelper.connectNode(node6, node7);
        DungeonHelper.connectNode(node7, node8);


        firstRoomChosen = false;
        fadeIn();
    }

    protected void initializeLevelSpecificChances() {
        shopRoomChance = 0.0F;
        restRoomChance = 0.0F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 1.0F;
        eliteRoomChance = 0.0F;
        smallChestChance = 0;
        mediumChestChance = 100;
        largeChestChance = 0;
        commonRelicChance = 0;
        uncommonRelicChance = 0;
        rareRelicChance = 100;
        colorlessRareChance = 0.3F;
        if (AbstractDungeon.ascensionLevel >= 12) {
            cardUpgradedChance = 0.5F;
        } else {
            cardUpgradedChance = 1.0F;
        }
    }

    protected void generateMonsters() {
        generateWeakEnemies(1);
        generateStrongEnemies(10);
        generateElites(10);

        // debug
        for (String s: monsterList){
            LogHelper.info("===InesMod:Chapter10：print monsterList：{}",s);
        }
    }

    protected void generateWeakEnemies(int i) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo("C10_W1_2ZS", 1.0F));
        monsters.add(new MonsterInfo("C10_W2_2XZW_1BJC", 1.0F));
        monsters.add(new MonsterInfo("C10_W3_1ZS_1BJC", 1.0F));

        MonsterInfo.normalizeWeights(monsters);
        populateMonsterList(monsters, i, false);
    }

    protected void generateStrongEnemies(int i) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo("C10_S1_3ZS", 1.0F));
        monsters.add(new MonsterInfo("C10_S2_2ZS_1BJC", 1.0F));

        // 可平替的怪物
        ArrayList<MonsterInfo> monsterOption1 = new ArrayList<>();
        monsterOption1.add(new MonsterInfo("C10_S3_1ZS_1GJ", 1.0F));
        monsterOption1.add(new MonsterInfo("C10_S4_3XZW_1GJ", 1.0F));
        monsterOption1.add(new MonsterInfo("C10_S5_1DZW_1GJ", 1.0F));
        monsters.add(monsterOption1.get(monsterRng.random(monsterOption1.size()-1))); // 从以上随机选一个


        MonsterInfo.normalizeWeights(monsters);
        populateFirstStrongEnemy(monsters, generateExclusions());
        populateMonsterList(monsters, i, false);
    }

    protected void generateElites(int i) {
        ArrayList<MonsterInfo> elitemonsters = new ArrayList<>();
        elitemonsters.add(new MonsterInfo("C10_E1_2SS", 3.0F));
        elitemonsters.add(new MonsterInfo("C10_E2_1DDG_1SS", 3.0F));

        // 可平替的怪物
        ArrayList<MonsterInfo> monsterOption1 = new ArrayList<>();
        monsterOption1.add(new MonsterInfo("C10_E3_2ZS_1DDG", 1.0F));
        monsterOption1.add(new MonsterInfo("C10_E4_3XZW_1DDG", 1.0F));
        elitemonsters.add(monsterOption1.get(monsterRng.random(monsterOption1.size()-1))); // 从以上随机选一个


        MonsterInfo.normalizeWeights(elitemonsters);
        populateMonsterList(elitemonsters, i, true);
    }

    protected ArrayList<String> generateExclusions() {
        ArrayList<String> retVal = new ArrayList<>();
        switch (monsterList.get(monsterList.size() - 1)) { // 上一个已选定的怪物（弱怪）
            case "C10_W1_2ZS":
                retVal.add("C10_S1_3ZS");
                break;
            case "C10_W2_2XZW_1BJC":
                retVal.add("C10_S2_2ZS_1BJC");
                break;
            case "C10_W3_1ZS_1BJC":
                retVal.add("C10_S2_2ZS_1BJC");
                break;
        }
        return retVal;
    }

    protected void initializeBoss() {
        bossList = new ArrayList();
        bossList.add("C10_B1_MFLD");
        bossList.add("C10_B1_MFLD");
        bossList.add("C10_B1_MFLD");
    }

    protected void initializeEventList() {}

    protected void initializeEventImg() {
        if (eventBackgroundImg != null) {
            eventBackgroundImg.dispose();
            eventBackgroundImg = null;
        }

        eventBackgroundImg = ImageMaster.loadImage("images/ui/event/panel.png");
    }

    protected void initializeShrineList() {}

    @Override
    public void loadSave(SaveFile saveFile) {
        LogHelper.info("===InesMod:Chapter10：loadSave：initializeMonsters===");
        MonsterHelper.initializeMonsters(); // 使继续上一次存档时，boss图标能正确渲染

        super.loadSave(saveFile);
    }
}