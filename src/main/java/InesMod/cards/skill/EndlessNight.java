package InesMod.cards.skill;

import InesMod.action.ApplyNonStackPowerAction;
import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.AdHocStrategyPower;
import InesMod.powers.EndlessNightPower;
import InesMod.powers.MasterTheGamePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：漫漫长夜
 */
public class EndlessNight extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(EndlessNight.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public EndlessNight() {
        super(ID,
                false,
                cardStrings,
                1,
                CardType.SKILL,
                CardRarity.UNCOMMON,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
        this.block = this.baseBlock = 9;

        this.cardsToPreview = new ShadowWhistle();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, this.block));
        this.addToBot(new GainEnergyAction(1));
        this.addToBot(new ApplyNonStackPowerAction(p, p, new EndlessNightPower(p, -1)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(3);
        }
    }
}
