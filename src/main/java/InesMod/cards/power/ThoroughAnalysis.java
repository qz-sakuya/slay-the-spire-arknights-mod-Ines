package InesMod.cards.power;

import InesMod.action.SetPowerAction;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.ThoroughAnalysisPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 中文卡名：分析透彻
 */
public class ThoroughAnalysis extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(ThoroughAnalysis.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public ThoroughAnalysis() {
        super(ID,
                false,
                cardStrings,
                1,
                CardType.POWER,
                CardRarity.UNCOMMON,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
        this.magicNumber = this.baseMagicNumber = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int tempNum = magicNumber;
        AbstractPower thoroughAnalysisPower = p.getPower(ThoroughAnalysisPower.ID);
        if (thoroughAnalysisPower != null && magicNumber > thoroughAnalysisPower.amount) {
            tempNum = thoroughAnalysisPower.amount; // 修正为更小值
        }

        addToBot(new SetPowerAction(p, p, new ThoroughAnalysisPower(p, tempNum), tempNum));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(-1);
        }
    }
}
