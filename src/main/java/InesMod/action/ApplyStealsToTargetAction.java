package InesMod.action;

import InesMod.cards.skill.LayTraps;
import InesMod.cards.skill.PreciseRecon;
import InesMod.helpers.LogHelper;
import InesMod.powers.*;
import InesMod.powers.player.*;
import InesMod.relics.FeintTripwire;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;

/**
 * 向敌方应用偷取 的效果
 * 判断手牌是否未满，然后抽1张牌
 */
public class ApplyStealsToTargetAction extends AbstractGameAction {
    public int consumeNum;
    public int amountBeforeReduce;
    public boolean triggerOther = true; // 是否触发其他效果

    public int strengthPerAmount;

    public ApplyStealsToTargetAction(AbstractCreature source, AbstractCreature target, int consumeNum, int strengthPerAmount, boolean triggerOther) {
        this(source, target, consumeNum, strengthPerAmount, triggerOther, 0);
    }

    public ApplyStealsToTargetAction(AbstractCreature source, AbstractCreature target, int consumeNum, int strengthPerAmount, boolean triggerOther, int amountBeforeReduce) {
        this.source = source;
        this.target = target;
        this.consumeNum = consumeNum;
        this.triggerOther = triggerOther;
        this.amountBeforeReduce = amountBeforeReduce;

        this.strengthPerAmount = strengthPerAmount;
    }


    @Override
    public void update() {
        LogHelper.info("===ApplyStealsToTargetAction: start，当前consumeNum:{}===",consumeNum);


        int strengthToApply = consumeNum * strengthPerAmount;
        int dexterityToApply = consumeNum;
        boolean applyDexterity = false;

        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof FeintTripwire){
                applyDexterity = true;
                break;
            }
        }

        if (applyDexterity){
            // 给当前目标减一次敏捷
            // 如果人工制品>=2层，则不挂“被偷取敏捷”
            AbstractPower powerToGet = target.getPower(ArtifactPower.POWER_ID);
            if (powerToGet != null && powerToGet.amount >= 2) {
                addToTop(new ApplyPowerAction(target, source, new DexterityPower(target, -dexterityToApply), -dexterityToApply));
            }
            else{
                addToTop(new ApplyPowerAction(target, source, new DexterityStolenPower(target, dexterityToApply), dexterityToApply,true));
                addToTop(new ApplyPowerAction(target, source, new DexterityPower(target, -dexterityToApply), -dexterityToApply));
            }
        }


        // 给当前目标减一次力量
        // 如果有人工制品，则不挂“被偷取力量”
        if (target.hasPower("Artifact")) {
            addToTop(new ApplyPowerAction(target, source, new StrengthPower(target, -strengthToApply), -strengthToApply));
        }
        else {
            addToTop(new ApplyPowerAction(target, source, new StrengthStolenPower(target, strengthToApply), strengthToApply,true));
            addToTop(new ApplyPowerAction(target, source, new StrengthPower(target, -strengthToApply), -strengthToApply));
        }







        if (triggerOther) {
            // 如果有分析透彻能力，判断是否给予易伤
            AbstractPower thoroughAnalysisPower = source.getPower(ThoroughAnalysisPower.ID);
            if (thoroughAnalysisPower instanceof AbstractInesPower
                    && amountBeforeReduce >= ((AbstractInesPower)thoroughAnalysisPower).secondAmount) {
                LogHelper.info("===ApplyStealsToTargetAction: 分析透彻给予易伤，层数:{}===",consumeNum);

                thoroughAnalysisPower.flash();
                int numToAdd = consumeNum * thoroughAnalysisPower.amount;
                addToBot(new ApplyPowerAction(target, source, new VulnerablePower(target, numToAdd, false), numToAdd));
            }

            // 如果手牌中有 精准探查 或 布设陷阱，触发效果
            if (source instanceof AbstractPlayer){
                AbstractPlayer p = (AbstractPlayer)source;
                for (AbstractCard c : p.hand.group) {
                    // 精准探查
                    if (c.cardID.equals(PreciseRecon.ID)) {
                        LogHelper.info("===ApplyStealsToTargetAction: 触发精准探查===");
                        addToBot(new ApplyPowerAction(source, source, new InterPower(source, c.magicNumber), c.magicNumber));
                    }

                    // 布设陷阱
                    if (c.cardID.equals(LayTraps.ID)) {
                        LogHelper.info("===ApplyStealsToTargetAction: 触发布设陷阱===");
                        addToBot(new ApplyPowerAction(target, source, new VulnerablePower(target, c.magicNumber, false), c.magicNumber));
                    }
                }
            }


        }

        this.isDone = true;
    }

}
