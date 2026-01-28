package InesMod.cards.skill;

import InesMod.action.SetPowerAction;
import InesMod.action.SetPowerSecondAmountAction;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import InesMod.powers.player.PhotographicMemoryPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 中文卡名：过目不忘
 */
public class PhotographicMemory extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(PhotographicMemory.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public PhotographicMemory() {
        super(ID,
                false,
                cardStrings,
                2,
                CardType.SKILL,
                CardRarity.RARE,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
        this.magicNumber = this.baseMagicNumber = 1;

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPower powerToGet = p.getPower(PhotographicMemoryPower.ID);
        if (powerToGet instanceof AbstractInesPower){
            AbstractInesPower photographicMemoryPower = (AbstractInesPower) powerToGet;

            if (magicNumber < photographicMemoryPower.secondAmount) {
                // 修正为更小值
                addToBot(new SetPowerSecondAmountAction(p, p, PhotographicMemoryPower.ID, magicNumber, true));
            }
        }else{
            // 效果不可叠加
            addToBot(new SetPowerAction(p, p, new PhotographicMemoryPower(p, 1, magicNumber), 1));
        }
    }




    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(-1);
        }
    }
}
