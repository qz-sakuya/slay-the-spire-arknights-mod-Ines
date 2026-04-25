package InesMod.action;

import InesMod.helpers.LogHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;


public class ResetMoveAction extends AbstractGameAction {
    public ResetMoveAction(AbstractMonster m) {
        this.m = m;
        this.duration = 0.1F;
    }

    @Override
    public void update() {
        this.tickDuration();
        if(this.isDone){
            m.createIntent();
        }
    }

    AbstractMonster m;
}

