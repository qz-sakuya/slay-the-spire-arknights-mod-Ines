package InesMod.cards.power;

import InesMod.action.SetPowerAction;
import InesMod.action.SetPowerSecondAmountAction;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import InesMod.powers.ThoroughAnalysisPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
        AbstractPower powerToGet = p.getPower(ThoroughAnalysisPower.ID);
        if (powerToGet instanceof AbstractInesPower){
            AbstractInesPower thoroughAnalysisPower = (AbstractInesPower) powerToGet;

            if (magicNumber < thoroughAnalysisPower.secondAmount) {
                // 修正条件为更小值
                addToBot(new SetPowerSecondAmountAction(p, p, ThoroughAnalysisPower.ID, magicNumber, true));
            }
        }else{
            // 效果不可叠加
            addToBot(new SetPowerAction(p, p, new ThoroughAnalysisPower(p, 1, magicNumber), 1));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(-1);
        }
    }
}
