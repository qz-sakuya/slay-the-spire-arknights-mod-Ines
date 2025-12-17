package InesMod.action;

import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.LogHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

/**
 * 背影 的效果
 */
public class SilhouetteAction extends AbstractGameAction {
    public SilhouetteAction(AbstractPlayer source) {
        this.source = source;
        this.actionType = ActionType.CARD_MANIPULATION;
    }


    @Override
    public void update() {
        LogHelper.info("===SilhouetteAction：start===");
        int cnt = 0;


        // 获取玩家手牌列表
        ArrayList<AbstractCard> handGroup = AbstractDungeon.player.hand.group;

        // 从后往前遍历
        for (int i = handGroup.size() - 1; i >= 0; i--) {
            AbstractCard c = handGroup.get(i);
            if (c.cardID.equals(ShadowWhistle.ID)) {
                cnt++;
                // 将影哨移入抽牌堆
                AbstractDungeon.player.hand.moveToDeck(c, true);
            }
        }

        // 抽等量牌
        this.addToBot(new DrawCardAction(source, cnt));

        this.isDone = true;
    }


}