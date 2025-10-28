package InesMod.powers;

import InesMod.cards.AbstractInesCard;
import InesMod.helpers.ModHelper;
import InesMod.modcore.InesModMain;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.HashSet;
import java.util.Set;

/**
 * 中文名：偷取
 */
public class StealsPower extends AbstractInesPower {
    public static final String ID = ModHelper.nameToId(StealsPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    private int consumeNum; // 一次偷取中，应用偷取的层数
    private final Set<AbstractCreature> stolenTarget = new HashSet<>(); // 一次偷取中，已经被偷取的怪物id

    public StealsPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
        consumeNum = 0;
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount > 999) {
            this.amount = 999;
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        InesModMain.logger.info(descriptions[0]);
        this.description = String.format(descriptions[0], 1, 1); // TODO：偷取效果提升后改写
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        InesModMain.logger.info("===StealsPower: onUseCard===");
        if (card.type == AbstractCard.CardType.ATTACK) {
            // 获取消耗偷取的层数，如果没有则默认1
            consumeNum = 1;
            if (card instanceof AbstractInesCard){
                consumeNum = ((AbstractInesCard) card).consumeSteals;
            }

            if (consumeNum > this.amount) {
                consumeNum = this.amount;
            }

        }
    }



    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        InesModMain.logger.info("===StealsPower: onAttack===");
        InesModMain.logger.info("===StealsPower: consumeNum:"+consumeNum);

        if (consumeNum > 0
                && !stolenTarget.contains(target)
                && damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
            // 给当前目标减一次力量
            addToBot(new ApplyPowerAction(target, owner, new StrengthPower(target, -consumeNum), -consumeNum));
            addToBot(new ApplyPowerAction(target, owner, new StrengthStolenPower(target, consumeNum), consumeNum));
            stolenTarget.add(target); // 记录该目标
        }
    }


    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        InesModMain.logger.info("===StealsPower: onAfterUseCard===");

        flash();
        // 给自己加一次力量
        if (consumeNum > 0){
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, consumeNum), consumeNum));
            addToBot(new ApplyPowerAction(owner, owner, new StrengthStealPower(owner, consumeNum), consumeNum));
        }
        addToBot(new ReducePowerAction(this.owner, this.owner, StealsPower.ID, consumeNum));

        consumeNum = 0;
        stolenTarget.clear();
    }
}
