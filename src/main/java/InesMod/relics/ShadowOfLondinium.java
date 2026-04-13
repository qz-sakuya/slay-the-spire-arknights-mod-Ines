package InesMod.relics;

import InesMod.helpers.ConfigHelper;
import InesMod.helpers.PathHelper;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.monsterRng;

/**
 * 中文名：伦蒂尼姆之影
 */
public class ShadowOfLondinium extends AbstractInesRelic {
    public static final String ID = PathHelper.nameToId(ShadowOfLondinium.class.getSimpleName());

    // 是否禁用额外层级
    public boolean banExtraLevel = false;

    // 是否固定额外层级
    public boolean setExtraLevelBoss = false;

    // 即将进入的额外层级
    // 0 -> Chapter 10
    // 1 -> Chapter 11
    // 2 -> Chapter 12
    // 3 -> Chapter 13
    private int levelType = 0;

    // 是否开启boss连战
    public boolean bossRush = false;

    public boolean needToRandom = false;


    public ShadowOfLondinium(){
        super(ID, false, false, RelicTier.SPECIAL, LandingSound.FLAT);

        banExtraLevel = ConfigHelper.banExtraLevel;

        if (ConfigHelper.setExtraLevelBoss){
            setExtraLevelBoss = true;
            if (ConfigHelper.setExtraLevelBossTo == -1){
                levelType = 0;
                bossRush = true;
            }
            else{
                levelType = ConfigHelper.setExtraLevelBossTo;
            }
        }
        else {
            needToRandom = true;
        }

        this.description = this.getUpdatedDescription();
    }

    public int getNextLevel(){
        if (needToRandom){
            initRandomLevelType();
        }

        int res = levelType;

        if (bossRush){
            updateNextBossRush();
        }

        return res;
    }



    private void initRandomLevelType(){
        // 随机决定
        // 目前只有一个额外层级
        levelType = monsterRng.random(0);

        needToRandom = false;
    }



    private void updateNextBossRush(){
        levelType += 1;
    }


    // 遗物初始描述
    @Override
    public String getUpdatedDescription() {
        String text = this.DESCRIPTIONS[0];

        if (ConfigHelper.banExtraLevel){
            text += this.DESCRIPTIONS[1];
        }
        else if(ConfigHelper.setExtraLevelBoss){
            if(ConfigHelper.setExtraLevelBossTo == -1){
                text += this.DESCRIPTIONS[2];
            }
            else if(ConfigHelper.setExtraLevelBossTo == 0){
                text += this.DESCRIPTIONS[3];
            }
        }

        return text;
    }




}