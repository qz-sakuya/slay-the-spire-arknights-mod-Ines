package InesMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

/**
 * 修改怪物在怪物list中的位置
 * 会覆盖目标位置的怪物
 */
public class MoveMonsterToIndexAction extends AbstractGameAction {
    public AbstractMonster monster;
    public int targetIndex;

    public MoveMonsterToIndexAction(AbstractMonster monster, int targetIndex) {
        this.monster = monster;
        this.targetIndex = targetIndex;
    }

    public void update() {
        ArrayList<AbstractMonster> monstersList = AbstractDungeon.getMonsters().monsters;
        monstersList.remove(monster);
        monstersList.set(targetIndex, monster);

        this.isDone = true;
    }
}
