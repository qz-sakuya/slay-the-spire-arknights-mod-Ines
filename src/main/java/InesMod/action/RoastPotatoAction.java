package InesMod.action;

import InesMod.cards.status.ShadowWhistle;
import InesMod.relics.RoastPotato;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import java.util.ArrayList;


public class RoastPotatoAction extends AbstractGameAction {
    RoastPotato roastPotato;
    public RoastPotatoAction(RoastPotato roastPotato) {
        this.roastPotato = roastPotato;
    }

    @Override
    public void update() {

        int size = AbstractDungeon.player.hand.size();
        if (size > 0) {
            roastPotato.flash();

            addToTop(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(size, true), DamageInfo.DamageType.THORNS, AttackEffect.FIRE));
//            addToTop(new WaitAction(0.5F));
//            for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
//                if (!m.isDeadOrEscaped()) {
//                    addToTop(new VFXAction(new ExplosionSmallEffect(m.hb.cX, m.hb.cY), 0.1F));
//                }
//            }





        }


        this.isDone = true;
    }

}
