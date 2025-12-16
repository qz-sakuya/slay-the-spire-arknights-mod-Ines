package InesMod.cards.power;

import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.MercenaryTacticsPower;
import InesMod.powers.ShadowTerritoryPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：佣兵手段
 */
public class MercenaryTactics extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(MercenaryTactics.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public MercenaryTactics() {
        super(ID,
                false,
                cardStrings,
                2,
                CardType.POWER,
                CardRarity.UNCOMMON,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
        this.magicNumber = this.baseMagicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new MercenaryTacticsPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(1);
        }
    }
}
