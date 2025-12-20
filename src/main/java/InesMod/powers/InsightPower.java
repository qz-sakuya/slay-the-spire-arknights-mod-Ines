package InesMod.powers;

import InesMod.action.ApplyStealsToTargetAction;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.truth.TruthReward;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 中文名：洞悉
 * 和 StealsPower 有相似代码，但独立上偷取效果。这么做的目的是，当没有 StealsPower 实例时，也能正常触发。
 */
public class InsightPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(InsightPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    private int consumeNum; // 一次偷取中，消耗偷取的层数
    private final Set<AbstractCreature> stolenTarget = new HashSet<>(); // 一次偷取中，已经被偷取的怪物id

    public InsightPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);

    }



    @Override
    public void updateDescription() {
        String text = String.format(descriptions[0], this.amount, this.amount);

        if (owner instanceof Ines) {
            Ines inesOwner = (Ines) owner;
            text += String.format(descriptions[1], inesOwner.counterForInsight, inesOwner.needForInsight);
        }

        this.description = text;
    }



    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        flash();
        consumeNum = this.amount;
    }



    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (consumeNum > 0
                && !stolenTarget.contains(target)
                && target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
            // 不参与其他效果
            addToBot(new ApplyStealsToTargetAction(owner, target, consumeNum, false));

            stolenTarget.add(target); // 记录该目标
        }
    }


    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
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
            addToBot(new ApplyPowerAction(owner, owner, new StrengthStealPower(owner, strengthToApply), strengthToApply));

        }


        consumeNum = 0;
        stolenTarget.clear();
    }

    // 战斗结束后转为等量真相
    @Override
    public void onVictory(){
        ArrayList<RewardItem> rewards = AbstractDungeon.getCurrRoom().rewards;
        rewards.add(0, new TruthReward(amount, true));
    }
}
