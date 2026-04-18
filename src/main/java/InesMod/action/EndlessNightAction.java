package InesMod.action;

import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.LogHelper;
import InesMod.powers.player.EndlessNightPower;
import InesMod.powers.player.InterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 漫漫长夜 的效果
 */
public class EndlessNightAction extends AbstractGameAction {
    EndlessNightPower power;

    public EndlessNightAction(EndlessNightPower power) {
        this.power = power;
    }



    @Override
    public void update() {
        boolean successAddRetain = false;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            // 保留所有影哨
            if (c.cardID.equals(ShadowWhistle.ID)) {
                c.retain = true;
                successAddRetain = true;
            }
        }
        if (successAddRetain) {
            power.flash();
        }

        this.isDone = true;
    }

}
