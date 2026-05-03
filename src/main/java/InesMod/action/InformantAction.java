package InesMod.action;

import InesMod.powers.player.InformantPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

/**
 * 线人 的效果
 */
public class InformantAction extends AbstractGameAction {
    private ArrayList<AbstractCard> cards = new ArrayList<>();
    private AbstractPlayer p;

    public InformantAction(AbstractPlayer p,ArrayList<AbstractCard> cards) {
        this.cards = new ArrayList<>(cards);
        this.p = p;
    }

    public void update() {
        AbstractPower informantPower = p.getPower(InformantPower.ID);
        if (informantPower != null) {
            informantPower.flash();
        }

        ArrayList<AbstractCard> cardList = new ArrayList<>();
        for (AbstractCard c : cards) {
            if (AbstractDungeon.player.drawPile.contains(c)
                    || AbstractDungeon.player.discardPile.contains(c)) {
                cardList.add(c);
            }
        }
        addToTop(new SmartMoveToHandAction(cardList));

        addToTop(new RemoveSpecificPowerAction(p, p, InformantPower.ID));

        this.isDone = true;
    }
}
