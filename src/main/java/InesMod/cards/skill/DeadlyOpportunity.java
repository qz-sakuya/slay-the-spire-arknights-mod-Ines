package InesMod.cards.skill;

import InesMod.action.ApplyNonStackPowerAction;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.player.DeadlyOpportunityPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 中文卡名：致命契机
 */
public class DeadlyOpportunity extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(DeadlyOpportunity.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public DeadlyOpportunity() {
        super(ID,
                false,
                cardStrings,
                0,
                CardType.SKILL,
                CardRarity.UNCOMMON,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPower powerToGet = p.getPower(DeadlyOpportunityPower.ID);
        if (powerToGet != null) {
            DeadlyOpportunityPower deadlyOpportunityPower = (DeadlyOpportunityPower) powerToGet;
            // 已经有未强化，则修正为强化
            if (!deadlyOpportunityPower.isUpgrade && this.upgraded){
                deadlyOpportunityPower.isUpgrade = true;
                deadlyOpportunityPower.flash();
                deadlyOpportunityPower.updateDescription();
            }
        }
        else {
            addToBot(new ApplyNonStackPowerAction(p, p, new DeadlyOpportunityPower(p, -1, this.upgraded)));
        }

    }





    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
