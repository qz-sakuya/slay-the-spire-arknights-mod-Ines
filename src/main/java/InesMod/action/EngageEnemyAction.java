package InesMod.action;

import InesMod.cards.status.ShadowWhistle;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * 接敌 的效果
 */
public class EngageEnemyAction extends AbstractGameAction {
    private final int block;
    private final int magicNumber;

    public EngageEnemyAction(AbstractCreature source, AbstractCreature target, int block, int magicNumber) {
        this.block = block;
        this.magicNumber = magicNumber;
        this.source = source;
        this.target = target;
    }

    public void update() {
        int cnt = 0;
        // 遍历手牌
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.cardID.equals(ShadowWhistle.ID)) {
                cnt++;
                break; // 可以提前退出循环
            }
        }

        int tempBlock = block;
        if (cnt == 0) {
            tempBlock += magicNumber;
        }

        this.addToTop(new GainBlockAction(source, source, tempBlock));

        this.isDone = true;
    }
}
