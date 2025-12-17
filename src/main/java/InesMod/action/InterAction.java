package InesMod.action;

import InesMod.helpers.LogHelper;
import InesMod.powers.InterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 情报 的效果
 * 判断手牌是否未满，然后抽1张牌
 */
public class InterAction extends AbstractGameAction {
    int amount;

    public InterAction(AbstractPlayer source, int drawAmount) {
        this.source = source;
        this.amount = drawAmount;
    }



    @Override
    public void update() {
        LogHelper.info("===情报action：开始===");
        AbstractPlayer p = (AbstractPlayer)source;

        if (!p.hasPower("No Draw") && p.hand.size() < 10) {
            LogHelper.info("===情报action：抽牌上限未满===");
            int cardToDraw = 1;

            // 如果有情报
            AbstractPower powerToFind = p.getPower(InterPower.ID);
            if (powerToFind != null) {
                LogHelper.info("===情报action：具有情报，层数：{}===",powerToFind.amount);

                int cardCanDraw =  AbstractDungeon.player.drawPile.group.size()
                        + AbstractDungeon.player.discardPile.group.size();

                if (cardCanDraw > 0) {
                    LogHelper.info("===情报action：具有可抽的牌，张数：{}===",cardCanDraw);

                    powerToFind.flash();
                    addToTop(new DrawCardAction(source, cardToDraw));
                    addToTop(new ReducePowerAction(this.source, this.source, InterPower.ID, cardToDraw));
                }
            }
        }

        this.isDone = true;
    }

}
