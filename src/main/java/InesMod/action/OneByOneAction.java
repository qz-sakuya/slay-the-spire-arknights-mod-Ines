package InesMod.action;

import InesMod.cards.attack.OnTheBrink;
import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.LogHelper;
import InesMod.powers.MasterTheGamePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

/**
 * 重置一触即发
 */
public class OneByOneAction extends AbstractGameAction {
    public OneByOneAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> attackGroup = new ArrayList<>();

        // 遍历手牌
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.type == AbstractCard.CardType.ATTACK && c.costForTurn > 0) {
                attackGroup.add(c);
            }
        }

        if (!attackGroup.isEmpty()) {
            // 随机选取一张攻击牌
            int randomIndex = AbstractDungeon.cardRandomRng.random(attackGroup.size()-1);
            AbstractCard randomCard = attackGroup.get(randomIndex);

            randomCard.setCostForTurn(randomCard.costForTurn - amount);
        }

        this.isDone = true;
    }

}
