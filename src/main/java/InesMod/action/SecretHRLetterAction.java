package InesMod.action;

import InesMod.helpers.LogHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

/**
 * SecretHRLetter 的动作
 */
public class SecretHRLetterAction extends AbstractGameAction {


    public SecretHRLetterAction() {

    }

    public void update() {
        // 遍历手牌
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            c.setCostForTurn(c.costForTurn - 2); // 耗能-2
        }


        this.isDone = true;
    }
}
