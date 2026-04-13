package InesMod.helpers;

import InesMod.dungeons.LevelChapter10;
import InesMod.monsters.Chapter10.*;
import basemod.BaseMod;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterInfo;

public class MonsterHelper {
    public static void initializeMonsters() {
        // ===第10章===
        addMonsterAndEncounter("C10_W1_2ZS", LevelChapter10.ID, false,
                new AbstractMonster[] {
                        new SarkazHeirbearerWarrior(-200.0F, 0.0F),
                        new SarkazHeirbearerWarrior(100.0F, 0.0F)
                }
        );

        addMonsterAndEncounter("C10_W2_2XZW_1BJC", LevelChapter10.ID, false,
                new AbstractMonster[] {
                        new TouchOfSanguinarch(-350.0F, 0.0F),
                        new TouchOfSanguinarch(-100.0F, 0.0F),
                        new SarkazHeirbearerASV(150.0F, 0.0F)
                }
        );

        addMonsterAndEncounter("C10_W3_1ZS_1BJC", LevelChapter10.ID,false,
                new AbstractMonster[] {
                        new SarkazHeirbearerWarrior(-200.0F, 0.0F),
                        new SarkazHeirbearerASV(100.0F, 0.0F)
                }
        );

        addMonsterAndEncounter("C10_S1_3ZS", LevelChapter10.ID,false,
                new AbstractMonster[] {
                        new SarkazHeirbearerWarrior(-350.0F, 0.0F),
                        new SarkazHeirbearerWarrior(-100.0F, 0.0F),
                        new SarkazHeirbearerWarrior(150.0F, 0.0F)
                }
        );

        addMonsterAndEncounter("C10_S2_2ZS_1BJC", LevelChapter10.ID,false,
                new AbstractMonster[] {
                        new SarkazHeirbearerWarrior(-350.0F, 0.0F),
                        new SarkazHeirbearerWarrior(-100.0F, 0.0F),
                        new SarkazHeirbearerASV(150.0F, 0.0F)
                }
        );

        addMonsterAndEncounter("C10_S3_1ZS_1GJ", LevelChapter10.ID,false,
                new AbstractMonster[] {
                        new SarkazHeirbearerWarrior(-200.0F, 0.0F),
                        new SarkazHeirbearerArtificer(100.0F, 0.0F)
                }
        );

        addMonsterAndEncounter("C10_S4_3XZW_1GJ", LevelChapter10.ID,false,
                new AbstractMonster[] {
                        new TouchOfSanguinarch(-600.0F, 0.0F),
                        new TouchOfSanguinarch(-350.0F, 0.0F),
                        new TouchOfSanguinarch(-100.0F, 0.0F),
                        new SarkazHeirbearerArtificer(150.0F, 0.0F)
                }
        );

        addMonsterAndEncounter("C10_E1_2SS", LevelChapter10.ID,true,
                new AbstractMonster[] {
                        new SarkazHeirbearerChainCaster(-200.0F, 0.0F),
                        new SarkazHeirbearerChainCaster(100.0F, 0.0F),
                }
        );

        addMonsterAndEncounter("C10_E2_1DDG_1SS", LevelChapter10.ID,true,
                new AbstractMonster[] {
                        new SarkazHeirbearerHatedrinker(-200.0F, 0.0F),
                        new SarkazHeirbearerChainCaster(100.0F, 0.0F),
                }
        );

        addMonsterAndEncounter("C10_E3_2ZS_1DDG", LevelChapter10.ID,true,
                new AbstractMonster[] {
                        new SarkazHeirbearerWarrior(-400.0F, 0.0F),
                        new SarkazHeirbearerWarrior(-150.0F, 0.0F),
                        new SarkazHeirbearerHatedrinker(150.0F, 0.0F),
                }
        );

        addMonsterAndEncounter("C10_E4_3XZW_1DDG", LevelChapter10.ID,true,
                new AbstractMonster[] {
                        new TouchOfSanguinarch(-600.0F, 0.0F),
                        new TouchOfSanguinarch(-370.0F, 0.0F),
                        new TouchOfSanguinarch(-140.0F, 0.0F),
                        new SarkazHeirbearerHatedrinker(150.0F, 0.0F)
                }
        );

        addBossAndEncounter("C10_B1_MFLD", LevelChapter10.ID,
                "InesModResources/img/dungeon/chapter10/C10_Boss_Icon.png",
                "InesModResources/img/dungeon/chapter10/C10_Boss_Icon_O.png",
                new AbstractMonster[] {
                        new SarkazHeirbearerWarrior(-400.0F, 0.0F),
                        new SarkazHeirbearerWarrior(-150.0F, 0.0F),
                        new Manfred(150.0F, 0.0F)
                }
        );
    }


    private static void addMonsterAndEncounter(String monsterID, String dungeonID, boolean isElite, AbstractMonster[] monsters) {
        addMonsterAndEncounter(monsterID, dungeonID, isElite, monsters, 1.0F);
    }

    private static void addMonsterAndEncounter(String monsterID, String dungeonID, boolean isElite, AbstractMonster[] monsters, float weight) {
        BaseMod.addMonster(monsterID, monsterID, () -> new MonsterGroup(monsters));

        if (isElite) {
            BaseMod.addEliteEncounter(dungeonID, new MonsterInfo(monsterID, weight));
        }
        else {
            BaseMod.addMonsterEncounter(dungeonID, new MonsterInfo(monsterID, weight));
        }
    }

    private static void addBossAndEncounter(String monsterID, String dungeonID, String mapIcon, String mapIconOutline, AbstractMonster[] monsters) {
        BaseMod.addMonster(monsterID, monsterID, () -> new MonsterGroup(monsters));

        BaseMod.addBoss(dungeonID, monsterID, mapIcon, mapIconOutline);
    }
}
