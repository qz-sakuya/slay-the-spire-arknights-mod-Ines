package InesMod.action;

import InesMod.cards.status.ShadowWhistle;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

/**
 * 修改卡牌费用 的效果
 */
public class UpdateCostAction extends AbstractGameAction {
    public AbstractCard card;
    public int amount;

    public UpdateCostAction(AbstractCard card, int amt) {
        this.card = card;
        this.amount = amt;
    }


    @Override
    public void update() {

        card.updateCost(amount);

        this.isDone = true;
    }


}