package InesMod.action;

import InesMod.cards.attack.ChaseWithShadow;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

/**
 * 乘影追击 的返回手牌效果
 */
public class ChaseWithShadowAction extends AbstractGameAction {
    private AbstractCard card;

    public ChaseWithShadowAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        int playedSize = AbstractDungeon.actionManager.cardsPlayedThisCombat.size();
        // 取倒数第二张
        if (playedSize >= 2) {
            AbstractCard prevCard = AbstractDungeon.actionManager.cardsPlayedThisCombat.get(playedSize - 2);
            if (prevCard != null) {

                // 非同名攻击卡
                if (prevCard.type == AbstractCard.CardType.ATTACK && !prevCard.cardID.equals(ChaseWithShadow.ID)) {
                    addToBot(new DiscardToHandAction(this.card));
                }
            }
        }


        this.isDone = true;
    }
}