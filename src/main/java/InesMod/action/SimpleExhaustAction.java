package InesMod.action;

import InesMod.characters.Ines;
import InesMod.patchs.moveToExhaustPilePatch;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

/**
 * 快速消耗一张卡 的动作
 * 修改自 moveToExhaustPile
 * 似乎无法触发 receivePostExhaust 的接口
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
            // AbstractDungeon.effectList.add(new ExhaustCardEffect(c));

            AbstractDungeon.player.exhaustPile.addToTop(targetCard);
            AbstractDungeon.player.onCardDrawOrDiscard(); // 原写法，不太明白但是照搬

            this.targetCard.exhaustOnUseOnce = false;
            this.targetCard.freeToPlayOnce = false;

            // 触发自定义回调
            moveToExhaustPilePatch.Work(targetCard);
        }

        this.isDone = true;
    }

}