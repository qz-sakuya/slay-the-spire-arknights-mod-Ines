package InesMod.powers;

import InesMod.action.ApplyStealsToTargetAction;
import InesMod.action.ReduceAndKeepPowerAction;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
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
 * 中文名：洞悉
 * 和 StealsPower 有相似代码，但独立上偷取效果。这么做的目的是，当没有 StealsPower 实例时，也能正常触发。
 */
public class InsightPower extends AbstractInesPower {
    public static final String ID = ModHelper.nameToId(InsightPower.class.getSimpleName());
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
            // 不参与 分析透彻 的效果，因为不算消耗偷取
            addToBot(new ApplyStealsToTargetAction(owner, target, consumeNum, 0));

            stolenTarget.add(target); // 记录该目标
        }
    }


    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (consumeNum > 0){

            // 给自己加一次力量
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, consumeNum), consumeNum));
            addToBot(new ApplyPowerAction(owner, owner, new StrengthStealPower(owner, consumeNum), consumeNum));
        }


        consumeNum = 0;
        stolenTarget.clear();
    }

    // TODO：战斗结束后转为等量真相
}
