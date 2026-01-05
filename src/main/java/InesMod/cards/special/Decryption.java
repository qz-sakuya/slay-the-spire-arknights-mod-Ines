package InesMod.cards.special;

import InesMod.cards.AbstractInesCard;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：解密
 * 衍生无色牌
 */
public class Decryption extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(Decryption.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public AbstractCard cardToDecryption = null;

    public Decryption() {
        super(ID,
                false,
                cardStrings,
                0,
                CardType.SKILL,
                CardRarity.SPECIAL,
                CardTarget.SELF,
                CardColor.COLORLESS);

        upgradeDescription();
    }
 
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (cardToDecryption != null) {
            if (AbstractDungeon.player.drawPile.contains(cardToDecryption)) {
                AbstractDungeon.player.drawPile.moveToHand(cardToDecryption);
            }
            else if (AbstractDungeon.player.discardPile.contains(cardToDecryption)) {
                AbstractDungeon.player.discardPile.moveToHand(cardToDecryption);
            }
        }
    }


    @Override
    public void applyPowers() {
        super.applyPowers();
        upgradeCardToPreview();
    }

    public void setCardToDecryption(AbstractCard card) {
        this.cardToDecryption = card;
        upgradeCardToPreview();
        upgradeDescription();
    }

    public void upgradeCardToPreview() {
        if (cardToDecryption != null) {
            this.cardsToPreview = this.cardToDecryption.makeStatEquivalentCopy();
        }
        else {
            this.cardsToPreview = null;
        }
    }

    public void upgradeDescription() {
        String cardText = cardStrings.EXTENDED_DESCRIPTION[1];
        if (cardToDecryption != null) {
            cardText = cardToDecryption.name;
        }

        if (!this.upgraded) {
            this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0] + cardText + cardStrings.DESCRIPTION;
        }
        else{
            this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0] + cardText + cardStrings.UPGRADE_DESCRIPTION;
        }

        this.initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.selfRetain = true;

            upgradeDescription();
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        Decryption card = (Decryption) super.makeStatEquivalentCopy();

        // 重新设置目标卡
        card.setCardToDecryption(this.cardToDecryption);
        return card;
    }
}
