package InesMod.action;

import InesMod.helpers.PathHelper;
import InesMod.modcore.InesModMain;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

/**
 * 快速消耗一张卡 的动作
 */
public class SimpleExhaustAction extends AbstractGameAction {
    private final AbstractCard targetCard;
    private final CardGroup group;

    public SimpleExhaustAction(AbstractCard targetCard, CardGroup group) {
        this.targetCard = targetCard;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.EXHAUST;
        this.group = group;
    }



    public void update() {
        if (this.group.contains(this.targetCard)) {
            for(AbstractRelic r : AbstractDungeon.player.relics) {
                r.onExhaust(targetCard);
            }

            for(AbstractPower p : AbstractDungeon.player.powers) {
                p.onExhaust(targetCard);
            }

            targetCard.triggerOnExhaust();

            // 获取私有方法引用
            ReflectionHacks.RMethod method = ReflectionHacks.privateMethod(CardGroup.class, "resetCardBeforeMoving", AbstractCard.class);
            method.invoke(this.group, targetCard); // 第一个参数是 CardGroup 实例，第二个是卡牌

            // 此处删除了显示特效

            AbstractDungeon.player.exhaustPile.addToTop(targetCard);
            AbstractDungeon.player.onCardDrawOrDiscard();

            this.targetCard.exhaustOnUseOnce = false;
            this.targetCard.freeToPlayOnce = false;
        }

        this.isDone = true;
    }

}