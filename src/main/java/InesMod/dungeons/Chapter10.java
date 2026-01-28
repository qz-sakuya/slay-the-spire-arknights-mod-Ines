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
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import com.megacrit.cardcrawl.rooms.TrueVictoryRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 额外层级
 * 对应主线第10章
 *
 * E：战士、城防炮手、萨卡兹车、凋亡术士（低攻。弹射：轮到自己攻击时，玩家有格挡则额外造成一次伤害）、大刀哥
 * 衍生：血裔
 * boss：曼弗雷德
 *
 *
 *
 * group：
 * W：2血裔+萨卡兹车、2战士、战士+萨卡兹车
 * S：3战士、战士+炮手、2战士+萨卡兹车、术士
 * E：2战士+大刀哥、2术士
 * B：2战士+曼弗雷德，二阶段还有2战士
 */
public class Chapter10 extends AbstractDungeon {
    private static final Logger logger = LogManager.getLogger(Chapter10.class.getName());

    public Chapter10(AbstractPlayer p, ArrayList<String> theList) {
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

    public Chapter10(AbstractPlayer p, SaveFile saveFile) {
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
        MapRoomNode node1 = DungeonHelper.createNode(3, 1, new MonsterRoomElite());
        MapRoomNode node2 = DungeonHelper.createNode(2, 2, new MonsterRoom());
        MapRoomNode node3 = DungeonHelper.createNode(2, 3, new EventRoom());
        MapRoomNode node4 = DungeonHelper.createNode(3, 4, new RestRoom());
        MapRoomNode node5 = DungeonHelper.createNode(4, 5, new TreasureRoom());
        MapRoomNode node6 = DungeonHelper.createNode(4, 6, new ShopRoom());
        MapRoomNode node7 = DungeonHelper.createNode(3, 7, new EventRoom());
        MapRoomNode node8 = DungeonHelper.createNode(3, 8, new MonsterRoomBoss());
        MapRoomNode node9 = DungeonHelper.createNode(3, 9, new TrueVictoryRoom());

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
        generateWeakEnemies(1);
        generateStrongEnemies(12);
        generateElites(10);
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