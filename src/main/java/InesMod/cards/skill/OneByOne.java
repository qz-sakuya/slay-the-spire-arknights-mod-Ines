package InesMod.cards.skill;

import InesMod.action.OneByOneAction;
import InesMod.action.ResetOnTheBrinkAction;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：一个接一个
 */
public class OneByOne extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(OneByOne.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public OneByOne() {
        super(ID,
                false,
                cardStrings,
                2,
                CardType.SKILL,
                CardRarity.RARE,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
        this.magicNumber = this.baseMagicNumber = 1;

        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 打出无效果
    }

    @Override
    public void onReceiveCardUsed(AbstractCard c) {
        if (c.type == AbstractCard.CardType.ATTACK) {
            addToBot(new OneByOneAction(magicNumber));
        }
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(1);
        }
    }
}
