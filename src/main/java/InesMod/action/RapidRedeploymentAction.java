package InesMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

/**
 * 快速重部署 的 使牌本回合变为0费 效果
 */
public class RapidRedeploymentAction extends AbstractGameAction {

    public RapidRedeploymentAction() {
        this.duration = 0.0F;
        this.actionType = AbstractGameAction.ActionType.WAIT;
    }

    public void update() {
        for (AbstractCard c : DrawCardAction.drawnCards) {
            c.setCostForTurn(-99);
        }
        this.isDone = true;
    }
}