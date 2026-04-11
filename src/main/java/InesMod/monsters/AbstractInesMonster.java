package InesMod.monsters;


import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractInesMonster extends AbstractMonster {
    public float baseWaitTime;
    public float waitTime;

    public AbstractInesMonster(String ID,
                               boolean useTmpArt,
                               MonsterStrings strings,
                               AbstractMonster.EnemyType type,
                               int health,
                               float hb_width,
                               float hb_height,
                               float x,
                               float y) {
        super(strings.NAME, ID, health, 0.0F, 0.0F, hb_width, hb_height, null, x, y);
        this.type = type;


        if (useTmpArt){
            setTestSpine();
        }

        setWaitTime(0.45F);
    }

    public void setImg(String ID, String imgFullName) {
        String monsterName = PathHelper.idToName(ID);
        LogHelper.info("===InesMod:AbstractInesMonster:setImg, 怪物名称：{}===",monsterName);

        String imgUrl = "InesModResources/model/monster/" + monsterName + '/' + imgFullName;

        this.img = ImageMaster.loadImage(imgUrl);
    }


    public void setSpine(String ID, String fileName, float divScale) {
        String monsterName = PathHelper.idToName(ID);
        LogHelper.info("===InesMod:AbstractInesMonster:setSpine, 怪物名称：{}===",monsterName);

        loadAnimation("InesModResources/model/monster/" + monsterName + '/' + fileName + ".atlas",
                "InesModResources/model/monster/" + monsterName + '/' + fileName + ".json",
                divScale);
        this.flipHorizontal = true;
    }


    // 设置缺省动画资源
    public void setTestSpine() {
        loadAnimation("InesModResources/model/monster/test/enemy_1345_tplamb.atlas",
                "InesModResources/model/monster/test/enemy_1345_tplamb.json",
                1.6F);

        this.flipHorizontal = true;
    }



    public void setWaitTime(float time) {
        this.baseWaitTime = time;
        this.setFastMode();
    }

    public void setFastMode() {
        if (Settings.FAST_MODE) {
            if (this.state != null) {
                this.state.setTimeScale(2.0F);
            }
            this.waitTime = this.baseWaitTime / 2;
        }
        else {
            if (this.state != null) {
                this.state.setTimeScale(1.0F);
            }
            this.waitTime = this.baseWaitTime;
        }
    }

    // 查找历史记录中往前数第n个移动记录是否与指定move相同
    // n >= 1
    protected boolean checkSpecificMove(int n, byte move) {
        if (n <= 0) {
            return false;
        }
        if (this.moveHistory.size() < n) {
            return false;
        }
        int targetIndex = this.moveHistory.size() - n;

        return this.moveHistory.get(targetIndex) == move;
    }

    // 检查最近的n个历史移动记录是否全部等于指定的move
    // n >= 1
    protected boolean checkAllMoves(int n, byte move) {
        if (n <= 0) {
            return false;
        }
        if (this.moveHistory.size() < n) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            int targetIndex = this.moveHistory.size() - 1 - i;
            if (this.moveHistory.get(targetIndex) != move) {
                return false;
            }
        }
        return true;
    }

    // 检查最近的n个历史移动记录是否含有指定的move
    // n >= 1
    protected boolean checkHaveMoves(int n, byte move) {
        if (n <= 0) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            int targetIndex = this.moveHistory.size() - 1 - i;
            if (targetIndex < 0){
                break;
            }
            if (this.moveHistory.get(targetIndex) == move) {
                return true;
            }
        }
        return false;
    }

    // 判定是否触发进阶增强
    public boolean ascensionForDamage() {
        if (this.type == EnemyType.NORMAL && AbstractDungeon.ascensionLevel >= 2){
            return true;
        }
        else if (this.type == EnemyType.ELITE && AbstractDungeon.ascensionLevel >= 3){
            return true;
        }
        else if (this.type == EnemyType.BOSS && AbstractDungeon.ascensionLevel >= 4){
            return true;
        }
        return false;
    }

    public boolean ascensionForHp() {
        if (this.type == EnemyType.NORMAL && AbstractDungeon.ascensionLevel >= 7){
            return true;
        }
        else if (this.type == EnemyType.ELITE && AbstractDungeon.ascensionLevel >= 8){
            return true;
        }
        else if (this.type == EnemyType.BOSS && AbstractDungeon.ascensionLevel >= 9){
            return true;
        }
        return false;
    }

    public boolean ascensionForMove() {
        if (this.type == EnemyType.NORMAL && AbstractDungeon.ascensionLevel >= 17){
            return true;
        }
        else if (this.type == EnemyType.ELITE && AbstractDungeon.ascensionLevel >= 18){
            return true;
        }
        else if (this.type == EnemyType.BOSS && AbstractDungeon.ascensionLevel >= 19){
            return true;
        }
        return false;
    }

}