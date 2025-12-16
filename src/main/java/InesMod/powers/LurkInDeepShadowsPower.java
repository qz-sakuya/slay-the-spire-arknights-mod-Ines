package InesMod.powers;

import InesMod.action.SelectHandCardAction;
import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：匿于深影
 */
public class LurkInDeepShadowsPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(LurkInDeepShadowsPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public LurkInDeepShadowsPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);

        this.priority = 4; // 排在 保留（优先级5）前面
    }


    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.amount);
    }


    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // 选择影哨放入抽牌堆
        if (isPlayer && !AbstractDungeon.player.hand.isEmpty()) {
            addToBot(new SelectHandCardAction(
                    "放入抽牌堆",
                    99,
                    c -> c.cardID != null && c.cardID.equals(ShadowWhistle.ID),
                    (selected) -> {
                        for (AbstractCard c : selected) {
                            if (c != null) {
                                AbstractDungeon.player.hand.moveToDeck(c, true); // 移入抽牌堆
                            }
                        }
                        AbstractDungeon.player.hand.refreshHandLayout();
                    },
                    true,
                    true
            ));
        }

    }

    @Override
    public void onCardDraw(AbstractCard card){
        if (card.cardID.equals(ShadowWhistle.ID)){
            flash();
            this.addToTop(new DrawCardAction(owner, 1));
        }
    }

}
