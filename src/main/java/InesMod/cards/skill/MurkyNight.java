package InesMod.cards.skill;

import InesMod.action.MurkyNightAction;
import InesMod.action.SimpleExhaustAction;
import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.AdHocStrategyPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：暗夜无明
 */
public class MurkyNight extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(MurkyNight.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public MurkyNight() {
        super(ID,
                false,
                cardStrings,
                2,
                CardType.SKILL,
                CardRarity.UNCOMMON,
                CardTarget.NONE,
                Ines.Enums.INES_CARD);
        this.magicNumber = this.baseMagicNumber = 1;

        this.cardsToPreview = new ShadowWhistle();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 打出无效果
    }

    @Override
    public void onReceiveCardUsed(AbstractCard c) {
        addToBot(new MurkyNightAction(this, c, magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }
}
