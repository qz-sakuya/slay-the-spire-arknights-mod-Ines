package InesMod.action;

import InesMod.powers.player.CageOfWarsPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 添加 CageOfWarsPower ，删除其它怪物的 CageOfWarsPower
 */
public class CageOfWarsAction extends AbstractGameAction {
    public CageOfWarsAction(AbstractPlayer player, AbstractMonster monster) {
        this.source = player;
        this.target = monster;
    }



    @Override
    public void update() {
        addToTop(new ApplyNonStackPowerAction(target, source, new CageOfWarsPower(target, 0)));


        for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (mo != null && !mo.isDeadOrEscaped() && mo != target) {
                addToTop(new RemoveSpecificPowerAction(mo, source, CageOfWarsPower.ID));
            }
        }


        this.isDone = true;
    }

}
