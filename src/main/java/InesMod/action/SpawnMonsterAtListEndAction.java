package InesMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 在怪物列表末尾生成怪物
 */
public class SpawnMonsterAtListEndAction extends AbstractGameAction {
    AbstractMonster m;
    boolean isMinion;

    public SpawnMonsterAtListEndAction(AbstractMonster m, boolean isMinion) {
        this.m = m;
        this.isMinion = isMinion;
    }

    public void update() {
        int currentSize = AbstractDungeon.getCurrRoom().monsters.monsters.size();
        addToTop(new SpawnMonsterAction(m, isMinion, currentSize));

        this.isDone = true;
    }
}
