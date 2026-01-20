package InesMod.cards.skill;

import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：白桦林的夜晚
 */
public class NightInWhiteBirchForest extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(NightInWhiteBirchForest.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    int updateAmount = -2;

    public NightInWhiteBirchForest() {
        super(ID,
                false,
                cardStrings,
                1,
                CardType.SKILL,
                CardRarity.RARE,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
        this.misc = 12;
        this.magicNumber = this.baseMagicNumber = misc;

        this.exhaust = true;
        this.tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        LogHelper.info("===NightInWhiteBirchForest om use: magicNumber={}===",this.magicNumber);
        LogHelper.info("===NightInWhiteBirchForest om use: misc修改前={}===",this.misc);
        addToBot(new HealAction(p, p, magicNumber));

        this.misc += this.updateAmount;
        if (this.misc < 0) {
            this.misc = 0;
        }
        this.applyPowers();
        this.baseMagicNumber = this.misc;
        this.magicNumber = this.baseMagicNumber;
        this.upgradedMagicNumber = true;
        LogHelper.info("===NightInWhiteBirchForest om use: misc修改后={}===",this.misc);


        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (!c.uuid.equals(this.uuid))
                continue;

            LogHelper.info("===NightInWhiteBirchForest in masterDeck: misc修改前={}===",c.misc);
            c.misc += this.updateAmount;
            if (c.misc < 0) {
                c.misc = 0;
            }
            c.applyPowers();
            c.baseMagicNumber = c.misc;
            c.magicNumber = c.baseMagicNumber;
            c.isMagicNumberModified = false;
            LogHelper.info("===NightInWhiteBirchForest in masterDeck: misc修改后={}===",c.misc);
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        NightInWhiteBirchForest card = (NightInWhiteBirchForest) super.makeStatEquivalentCopy();

        // 深拷贝该值
        LogHelper.info("===NightInWhiteBirchForest makeStatEquivalentCopy: 拷贝后misc={}===",card.misc);
        card.updateAmount = this.updateAmount;

        // 确保magicNumber正确
        card.baseMagicNumber = card.misc;
        card.magicNumber = card.baseMagicNumber;

        return card;
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.updateAmount = -1;

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
