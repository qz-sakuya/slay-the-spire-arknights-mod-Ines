package InesMod.cards.power;

import InesMod.action.SetPowerAction;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.ModHelper;
import InesMod.powers.AgentVanguardPower;
import InesMod.powers.InvisibilityPower;
import InesMod.powers.NoInvisibilityPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：阴云笼罩
 */
public class GloomyClouds extends AbstractInesCard {
    public static final String ID = ModHelper.nameToId(GloomyClouds.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public GloomyClouds() {
        super(ID,
                false,
                cardStrings,
                2,
                CardType.POWER,
                CardRarity.UNCOMMON,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
        this.magicNumber = this.baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new InvisibilityPower(p, magicNumber), magicNumber));
        addToBot(new SetPowerAction(p, p, new NoInvisibilityPower(p, 2), 2));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }
}
