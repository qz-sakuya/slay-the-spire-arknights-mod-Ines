package InesMod.action;

import InesMod.helpers.LogHelper;
import InesMod.monsters.Chapter10.Manfred;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;


public class SmartMoveToHandAction extends AbstractGameAction {
    public ArrayList<AbstractCard> cardList = new ArrayList<>();


    public SmartMoveToHandAction(AbstractCard card) {
        this.cardList = new ArrayList<>(cardList); // 复制一份
        this.cardList.add(card);
    }

    public SmartMoveToHandAction(ArrayList<AbstractCard> cardList){
        this.cardList = cardList;
    }

    @Override
    public void update() {
        LogHelper.info("===SmartMoveToHandAction：开始");

        for(AbstractCard c : cardList){
            if (c != null) {
                LogHelper.info("===SmartMoveToHandAction：处理卡牌：{}===", c.cardID);

                // 溢出弃牌
                if (AbstractDungeon.player.hand.size() >= 10) {
                    LogHelper.info("===SmartMoveToHandAction：溢出丢弃：{}===", c.cardID);

                    if (AbstractDungeon.player.drawPile.contains(c)) {
                        AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                    }
                    else if (AbstractDungeon.player.discardPile.contains(c)) {
                        AbstractDungeon.player.discardPile.moveToDiscardPile(c);
                    }
                    else if (AbstractDungeon.player.exhaustPile.contains(c)) {
                        AbstractDungeon.player.exhaustPile.moveToDiscardPile(c);
                    }

                    AbstractDungeon.player.createHandIsFullDialog();
                    continue;
                }

                LogHelper.info("===SmartMoveToHandAction：加入手牌：{}===", c.cardID);

                if (AbstractDungeon.player.drawPile.contains(c)) {
                    AbstractDungeon.player.drawPile.moveToHand(c);
                }
                else if (AbstractDungeon.player.discardPile.contains(c)) {
                    AbstractDungeon.player.discardPile.moveToHand(c);
                }
                else if (AbstractDungeon.player.exhaustPile.contains(c)) {
                    AbstractDungeon.player.exhaustPile.moveToHand(c);
                }
            }
        }


        this.isDone = true;
    }
}


