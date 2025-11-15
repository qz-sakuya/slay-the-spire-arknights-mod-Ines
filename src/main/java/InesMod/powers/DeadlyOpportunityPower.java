package InesMod.powers;

import InesMod.action.ApplyStealsToTargetAction;
import InesMod.action.RemoveHalfBlockAction;
import InesMod.action.SetBlockAction;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.HashSet;
import java.util.Set;

/**
 * 中文名：致命契机
 */
public class DeadlyOpportunityPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(DeadlyOpportunityPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public boolean isUpgrade = false;

    public DeadlyOpportunityPower(AbstractCreature owner, int amount, boolean isUpgrade) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
        this.isUpgrade = isUpgrade;
    }


    @Override
    public void updateDescription() {
        if (!isUpgrade){
            this.description = descriptions[0];
        }
        else{
            this.description = descriptions[1];
        }

    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * 2.0F; // 双倍伤害
        }
        return damage;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK && !card.purgeOnUse) {
            flash();
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, DeadlyOpportunityPower.ID));

            if (!isUpgrade){
                this.addToBot(new RemoveAllBlockAction(this.owner, this.owner));
            }
            else{
                this.addToBot(new RemoveHalfBlockAction(this.owner, this.owner));
            }

            // 结束你的回合
            this.addToBot(new PressEndTurnButtonAction());

        }

    }
}
