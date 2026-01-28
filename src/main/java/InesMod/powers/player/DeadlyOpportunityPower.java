package InesMod.powers.player;

import InesMod.action.DeadlyOpportunityAction;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

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

        // 排在力量后面（参考原版 双倍伤害 power）
        this.priority = 6;
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


            // 致命契机+手牌有影哨+打出具有弃牌的攻击牌（如必要代价），
            // 则影哨在afterUseCard前消耗且addToBot(起防Action)，再addToBot(致命契机Action)
            // 结论是影哨的防御会被清空

            // 掌控全局+手牌有影哨+隐匿+打出攻击牌，
            // 则影哨在afterUseCard后消耗且addToBot(起防Action)，此时起防Action晚于致命契机Action
            // 结论是影哨的防御会保留
            // 因为最多在隐匿下丢弃1张，因此保留作为机制
            addToBot(new DeadlyOpportunityAction((AbstractPlayer) this.owner, this.isUpgrade));
        }
    }
}
