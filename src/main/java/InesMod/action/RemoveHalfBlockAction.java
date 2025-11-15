package InesMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

/**
 * 移除一半格挡 的动作
 * 根据 GainBlockAction 修改
 */
public class RemoveHalfBlockAction extends AbstractGameAction {
    private static final float DUR = 0.25F;

    public RemoveHalfBlockAction(AbstractCreature target, AbstractCreature source) {
        setValues(target, source, this.amount);
        this.actionType = AbstractGameAction.ActionType.BLOCK;
        this.duration = 0.25F;
    }

    public void update() {
        if (!this.target.isDying && !this.target.isDead &&
                this.duration == 0.25F &&
                this.target.currentBlock > 0) {
            int newBlock = Math.floorDiv(this.target.currentBlock, 2);
            addToTop(new SetBlockAction(target, source, newBlock));
        }

        tickDuration();
    }
}