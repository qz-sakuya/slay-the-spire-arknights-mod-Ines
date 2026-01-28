package InesMod.dungeons;

import InesMod.helpers.DungeonHelper;
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
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 额外层级
 * 对应主线第13章
 *
 * 还没开发到这里
 * 记得注释
 */
public class Chapter13 extends AbstractDungeon {
    private static final Logger logger = LogManager.getLogger(Chapter13.class.getName());

    public Chapter13(AbstractPlayer p, ArrayList<String> theList) {
        super(NAME, "eyjafjalla:TheSiesta", p, theList);
        if (scene != null) {
            scene.dispose();
        }

        scene = new Chapter10Scene();
        fadeColor = Color.valueOf("140a1eff");
        sourceFadeColor = Color.valueOf("140a1eff");
        initializeLevelSpecificChances();
        mapRng = new Random(Long.valueOf(Settings.seed.longValue() + (AbstractDungeon.actNum * 300)));
        generateSpecialMap();
        CardCrawlGame.music.changeBGM(id);
    }

    public Chapter13(AbstractPlayer p, SaveFile saveFile) {
        super(NAME, p, saveFile);
        CardCrawlGame.dungeon = this;
        if (scene != null) {
            scene.dispose();
        }

        scene = new Chapter10Scene();
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
        MapRoomNode node1 = DungeonHelper.createNode(4, 1, new ShopRoom());
        MapRoomNode node2 = DungeonHelper.createNode(5, 2, new MonsterRoom());
        MapRoomNode node3 = DungeonHelper.createNode(4, 3, new MonsterRoom());
        MapRoomNode node4 = DungeonHelper.createNode(3, 2, new TreasureRoom());
        MapRoomNode node5 = DungeonHelper.createNode(2, 1, new MonsterRoom());
        MapRoomNode node6 = DungeonHelper.createNode(1, 2, new MonsterRoomElite());
        MapRoomNode node7 = DungeonHelper.createNode(2, 3, new RestRoom());
        MapRoomNode node8 = DungeonHelper.createNode(3, 4, new MonsterRoomBoss());
        MapRoomNode node9 = DungeonHelper.createNode(3, 5, new TrueVictoryRoom());

        map = DungeonHelper.createMap(node0,node2,node3,node4,node5,node6,node7,node8,node9);

        DungeonHelper.connectNode(node0, node1);
        DungeonHelper.connectNode(node1, node2);
        DungeonHelper.connectNode(node2, node3);
        DungeonHelper.connectNode(node3, node4);
        DungeonHelper.connectNode(node4, node5);
        DungeonHelper.connectNode(node5, node6);
        DungeonHelper.connectNode(node6, node7);
        DungeonHelper.connectNode(node7, node8);
        DungeonHelper.connectNode(node8, node9);

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
        monsterList = new ArrayList();
        monsterList.add("N_E_TQB");
        monsterList.add("N_E_CPS_CPS");
        monsterList.add("H_E_TQB_LTTQB");
        monsterList.add("H_E_CPS_LTTQB");
        Collections.shuffle(monsterList, monsterRng.random);

        eliteMonsterList = new ArrayList();
        eliteMonsterList.add("E_E_DGDGZ");
        eliteMonsterList.add("E_E_JMCPS_MTFKY");
        eliteMonsterList.add("E_E_CXXSS_4");
        Collections.shuffle(eliteMonsterList, monsterRng.random);
    }

    protected void generateWeakEnemies(int i) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo("N_E_TQB", 1.0F));
        monsters.add(new MonsterInfo("N_E_CPS_CPS", 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        populateMonsterList(monsters, i, false);
    }

    protected void generateStrongEnemies(int i) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo("H_E_TQB_LTTQB", 1.0F));
        monsters.add(new MonsterInfo("H_E_CPS_LTTQB", 1.0F));
        monsters.add(new MonsterInfo("H_E_TQB_LTTQB", 1.0F));
        monsters.add(new MonsterInfo("H_E_CPS_LTTQB", 1.0F));

        MonsterInfo.normalizeWeights(monsters);
        populateFirstStrongEnemy(monsters, generateExclusions());
        populateMonsterList(monsters, i, false);
    }

    protected void generateElites(int i) {
        ArrayList<MonsterInfo> elitemonsters = new ArrayList<>();
        elitemonsters.add(new MonsterInfo("Shield and Spear", 3.0F));
        elitemonsters.add(new MonsterInfo("E_E_JMCPS_MTFKY", 3.0F));
        elitemonsters.add(new MonsterInfo("E_E_JMCPS_MTFKY", 3.0F));

        MonsterInfo.normalizeWeights(elitemonsters);
        populateMonsterList(elitemonsters, i, true);
    }

    protected ArrayList<String> generateExclusions() {
        return new ArrayList<>();
    }

    protected void initializeBoss() {
        bossList = new ArrayList();
        bossList.add("eyjafjalla:Dolly");
        bossList.add("eyjafjalla:Dolly");
        bossList.add("eyjafjalla:Dolly");
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

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("eyjafjalla:TheSiesta");
    public static final String[] TEXT = uiStrings.TEXT;
    public static final String NAME = TEXT[0];
    public static final String ID = "eyjafjalla:TheSiesta";
}