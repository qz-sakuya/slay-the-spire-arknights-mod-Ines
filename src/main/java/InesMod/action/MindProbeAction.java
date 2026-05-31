package InesMod.action;

import InesMod.powers.player.IntelPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 心灵探查 的效果
 */
public class MindProbeAction extends AbstractGameAction {
    AbstractPlayer p;
    int initAmount;
    int updateAmount;

    public MindProbeAction(AbstractPlayer p, int initAmount, int updateAmount) {
        this.p = p;
        this.initAmount = initAmount;
        this.updateAmount = updateAmount;
    }

    public void update() {
        Iterator<AbstractMonster> var1 = (AbstractDungeon.getMonsters()).monsters.iterator();
        ArrayList<AbstractMonster.Intent> seenIntent = new ArrayList<>();

        if (!(AbstractDungeon.getMonsters()).monsters.isEmpty()) {
            while (var1.hasNext()) {
                AbstractMonster curMonster = var1.next();
                if (!curMonster.isDeadOrEscaped() && !seenIntent.contains(curMonster.intent)) {
                    seenIntent.add(curMonster.intent);
                }
            }
        }
        initAmount += seenIntent.size() * updateAmount;

        if (initAmount > 0) {
            addToBot(new ApplyPowerAction(p, p, new IntelPower(p, initAmount), initAmount));
        }


        this.isDone = true;
    }
}
