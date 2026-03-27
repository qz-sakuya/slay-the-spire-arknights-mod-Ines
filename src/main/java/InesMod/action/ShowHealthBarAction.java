package InesMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

/**
 * 延迟显示怪物血条
 */
public class ShowHealthBarAction extends AbstractGameAction {
    private AbstractMonster m;


    public ShowHealthBarAction(AbstractMonster target) {
        this.m = target;
    }

    public void update() {
        m.showHealthBar();

        this.isDone = true;
    }
}
