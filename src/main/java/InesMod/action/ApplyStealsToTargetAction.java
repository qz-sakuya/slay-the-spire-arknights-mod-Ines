package InesMod.action;

import InesMod.modcore.InesModMain;
import InesMod.powers.StrengthStolenPower;
import InesMod.powers.ThoroughAnalysisPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

/**
 * 向敌方应用偷取 的效果
 * 判断手牌是否未满，然后抽1张牌
 */
public class ApplyStealsToTargetAction extends AbstractGameAction {
    AbstractCreature source;
    AbstractCreature target;
    int consumeNum;
    int amountBeforeReduce;

    public ApplyStealsToTargetAction(AbstractCreature source, AbstractCreature target, int consumeNum, int amountBeforeReduce) {
        this.source = source;
        this.target = target;
        this.consumeNum = consumeNum;
        this.amountBeforeReduce = amountBeforeReduce;
    }




    @Override
    public void update() {
        // 给当前目标减一次力量
        // 如果有人工制品，则不挂“被偷取力量”
        if (!target.hasPower("Artifact")) {
            addToTop(new ApplyPowerAction(target, source, new StrengthStolenPower(target, consumeNum), consumeNum));
        }
        addToTop(new ApplyPowerAction(target, source, new StrengthPower(target, -consumeNum), -consumeNum));

        // 如果有分析透彻能力，判断是否给予易伤
        AbstractPower thoroughAnalysisPower = source.getPower(ThoroughAnalysisPower.ID);
        if (thoroughAnalysisPower != null && amountBeforeReduce >= thoroughAnalysisPower.amount) {
            InesModMain.logger.info("===ApplyStealsToTargetAction: 分析透彻给予易伤，层数:{}===",consumeNum);

            thoroughAnalysisPower.flash();
            addToBot(new ApplyPowerAction(target, source, new VulnerablePower(target, consumeNum, false), consumeNum));
        }

        this.isDone = true;
    }

}
