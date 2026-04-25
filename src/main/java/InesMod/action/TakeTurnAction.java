package InesMod.action;

import InesMod.monsters.Chapter10.Manfred;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class TakeTurnAction extends AbstractGameAction {
    public TakeTurnAction(AbstractMonster m){
        this.m = m;
    }

    @Override
    public void update() {
        if(m!=null){
            if(m instanceof Manfred){
                ((Manfred) m).takeTurn(true);
            }
            else{
                m.takeTurn();
            }
        }
        this.isDone = true;
    }

    AbstractMonster m;
}


