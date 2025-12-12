package InesMod.cards.skill;

import InesMod.action.ApplyNonStackPowerAction;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.RapidRedeploymentPower;
import InesMod.powers.ShiftFrontlinesPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：转移阵线
 */
public class ShiftFrontlines extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(ShiftFrontlines.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public ShiftFrontlines() {
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
        // 不可叠加
        addToBot(new ApplyNonStackPowerAction(p, p, new ShiftFrontlinesPower(p, 1, 0)));
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();

            this.selfRetain = true;

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
