package InesMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

/**
 * 绝密行动 自动打出时的操作
 */
public class TopSecretOperationAutoUseAction extends AbstractGameAction {
    private AbstractCard card;

    public TopSecretOperationAutoUseAction(AbstractCard card) {
        this.card = card;
    }

    public void update() {
        if (!this.card.hasEnoughEnergy() || !this.card.cardPlayable(null)){
            // 无法打出则消耗
            addToTop(new ExhaustSpecificCardAction(this.card, AbstractDungeon.player.hand));
        }
        else {
            // 立即消耗能量打出
            this.card.applyPowers();
            addToTop(new NewQueueCardAction(this.card, true, true, true));
            addToTop(new LoseEnergyAction(this.card.cost));
        }
        this.isDone = true;
    }
}
