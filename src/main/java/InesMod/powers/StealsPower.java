package InesMod.powers;

import InesMod.action.ApplyStealsToTargetAction;
import InesMod.action.ReduceAndKeepPowerAction;

import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.ConfigHelper;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.helpers.TutorialHelper;
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

import java.util.HashSet;
import java.util.Set;

/**
 * 中文名：偷取
 */
public class StealsPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(StealsPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    private int consumeNum; // 一次偷取中，消耗偷取的层数
    private final Set<AbstractCreature> stolenTarget = new HashSet<>(); // 一次偷取中，已经被偷取的怪物id

    public int amountBeforeReduce; // 一次偷取中，消耗偷取前的层数

    public StealsPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
        consumeNum = 0;
        amountBeforeReduce = 0;

        this.priority = 3; // 排在 洞悉（优先级5）及大多数power（优先级默认5）前面
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
        LogHelper.info("===StealsPower: onUseCard, 当前卡牌id{}===",card.cardID);
        consumeNum = 0;
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
            LogHelper.info("===StealsPower:  onUseCard，设置consumeNum为{}===",consumeNum);
        }
    }



    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        LogHelper.info("===StealsPower: onAttack===");

        if (consumeNum > 0
                && !stolenTarget.contains(target)
                && target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
            addToBot(new ApplyStealsToTargetAction(owner, target, consumeNum, true, amountBeforeReduce));

            stolenTarget.add(target); // 记录该目标
        }
    }


    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        LogHelper.info("===StealsPower: onAfterUseCard,当前卡牌id{}, 当前consumeNum{}===",card.cardID,consumeNum);
        if (consumeNum > 0){
            int strengthToApply = consumeNum;

            // 如果有 佣兵手段 ，提升力量效果
            AbstractPower mercenaryTacticsPower = owner.getPower(MercenaryTacticsPower.ID);
            if (mercenaryTacticsPower != null) {
                mercenaryTacticsPower.flash();
                strengthToApply += (mercenaryTacticsPower.amount * consumeNum);
            }

            // 给自己加一次力量
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strengthToApply), strengthToApply));
            addToBot(new ApplyPowerAction(owner, owner, new StrengthStealPower(owner, strengthToApply), strengthToApply,true));


            // 处理 洞悉 相关逻辑
            if (owner instanceof Ines) {
                Ines inesOwner = (Ines) owner;
                inesOwner.counterForInsight += consumeNum;
                while (inesOwner.counterForInsight >= inesOwner.needForInsight) {
                    inesOwner.counterForInsight -= inesOwner.needForInsight;
                    inesOwner.needForInsight += 2; // 每次获得洞悉，所需层数+2。

                    addToBot(new ApplyPowerAction(owner, owner, new InsightPower(owner, 1), 1));

                    // 显示教程
                    if (!ConfigHelper.tutorialClosed1) {
                        TutorialHelper.playTutorial1(owner);
                    }
                }

                // 如果有洞悉能力，更新其描述
                AbstractPower insightPower = owner.getPower(InsightPower.ID);
                if (insightPower != null) {
                    insightPower.updateDescription();
                }
            }

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

        // 延迟remove，避免onAfterUseCard不触发
        if (this.amount == 0){
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, StealsPower.ID));
        }
    }
}
