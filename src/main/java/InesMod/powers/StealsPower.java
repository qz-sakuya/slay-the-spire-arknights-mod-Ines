package InesMod.powers;

import InesMod.action.ReduceAndKeepPowerAction;
import InesMod.cards.AbstractInesCard;
import InesMod.helpers.ModHelper;
import InesMod.modcore.InesModMain;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

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

    private int amountBeforeReduce; // 一次偷取中，消耗偷取前的层数

    public StealsPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
        consumeNum = 0;
        amountBeforeReduce = 0;

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

            if (consumeNum > 0){
                flash();
                amountBeforeReduce = this.amount;
                addToTop(new ReduceAndKeepPowerAction(this.owner, this.owner, StealsPower.ID, consumeNum));
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


            // 如果有分析透彻能力，判断是否给予易伤
            AbstractPower thoroughAnalysisPower = owner.getPower(ThoroughAnalysisPower.ID);
            if (thoroughAnalysisPower != null && amountBeforeReduce >= thoroughAnalysisPower.amount) {
                thoroughAnalysisPower.flash();
                addToBot(new ApplyPowerAction(target, owner, new VulnerablePower(target, consumeNum, false), consumeNum));
            }





            stolenTarget.add(target); // 记录该目标
        }
    }


    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        InesModMain.logger.info("===StealsPower: onAfterUseCard===");
        if (consumeNum > 0){

            // 给自己加一次力量
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, consumeNum), consumeNum));
            addToBot(new ApplyPowerAction(owner, owner, new StrengthStealPower(owner, consumeNum), consumeNum));

            // 如果有情报官能力，获得 consumeNum层数 * 能力层数 的情报
            AbstractPower agentVanguardPower = owner.getPower(AgentVanguardPower.ID);
            if (agentVanguardPower != null) {
                agentVanguardPower.flash();
                int tempNum = consumeNum*agentVanguardPower.amount;

                addToBot(new ApplyPowerAction(owner, owner, new InterPower(owner, tempNum), tempNum));
            }


        }


        consumeNum = 0;
        stolenTarget.clear();

        // 延迟删除，避免onAfterUseCard不触发
        if (this.amount == 0){
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, StealsPower.ID));
        }
    }
}
