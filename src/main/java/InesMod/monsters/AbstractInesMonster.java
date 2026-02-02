package InesMod.monsters;


import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractInesMonster extends AbstractMonster {
    public AbstractInesMonster(String ID,
                               MonsterStrings strings,
                               AbstractMonster.EnemyType type,
                               int health,
                               float hb_width,
                               float hb_height,
                               float x,
                               float y) {
        super(strings.NAME, ID, health, 0.0F, 0.0F, hb_width, hb_height, null, x, y);
        this.type = type;

        // 设置缺省动画资源
        loadAnimation("InesModResources/model/monster/test/enemy_1345_tplamb.atlas",
                "InesModResources/model/monster/test/enemy_1345_tplamb.json",
                1.6F);
    }

    public void setSpine(String ID, String fileName, float divScale) {
        String monsterName = PathHelper.idToName(id);
        loadAnimation("InesModResources/model/monster/" + monsterName + '/' + fileName + ".atlas",
                "InesModResources/model/monster/" + monsterName + '/' + fileName + ".json",
                divScale);
        this.flipHorizontal = true;
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
}