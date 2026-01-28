package InesMod.cards.power;

import InesMod.action.ApplyNonStackPowerAction;
import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.player.LurkInDeepShadowsPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：匿于深影
 */
public class LurkInDeepShadows extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(LurkInDeepShadows.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public LurkInDeepShadows() {
        super(ID,
                false,
                cardStrings,
                2,
                CardType.POWER,
                CardRarity.RARE,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);

        this.cardsToPreview = new ShadowWhistle();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyNonStackPowerAction(p, p, new LurkInDeepShadowsPower(p, -1)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(1);
        }
    }
}
