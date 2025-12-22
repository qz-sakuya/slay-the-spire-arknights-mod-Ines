package InesMod.action;

import InesMod.powers.InformantPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;

/**
 * 线人 的效果
 */
public class InformantAction extends AbstractGameAction {
    private ArrayList<AbstractCard> cards = new ArrayList<>();
    private AbstractPlayer p;

    public InformantAction(AbstractPlayer p,ArrayList<AbstractCard> cards) {
        this.cards = cards;
        this.p = p;
    }

    public void update() {
        AbstractPower informantPower = p.getPower(InformantPower.ID);
        if (informantPower != null) {
            informantPower.flash();
        }

        for (AbstractCard c : cards) {
            if (AbstractDungeon.player.drawPile.contains(c)) {
                AbstractDungeon.player.drawPile.moveToHand(c);
            }
            else if (AbstractDungeon.player.discardPile.contains(c)) {
                AbstractDungeon.player.discardPile.moveToHand(c);
            }
        }
        addToTop(new RemoveSpecificPowerAction(p, p, InformantPower.ID));

        this.isDone = true;
    }
}
