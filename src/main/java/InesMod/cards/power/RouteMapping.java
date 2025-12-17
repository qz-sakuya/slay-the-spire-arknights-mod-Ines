package InesMod.cards.power;

import InesMod.action.ApplyNonStackPowerAction;
import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.RouteMappingPower;
import InesMod.powers.ShadowTerritoryPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：路线绘制
 */
public class RouteMapping extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(RouteMapping.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public RouteMapping() {
        super(ID,
                false,
                cardStrings,
                2,
                CardType.POWER,
                CardRarity.RARE,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
        this.magicNumber = this.baseMagicNumber = 1;

        this.cardsToPreview = new ShadowWhistle();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyNonStackPowerAction(p, p, new RouteMappingPower(p, -1)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(1);


//            this.isInnate = true;
//
//            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
//            this.initializeDescription();
        }
    }
}
