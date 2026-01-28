package InesMod.cards.skill;

import InesMod.action.SelectHandCardAction;
import InesMod.cards.AbstractInesCard;
import InesMod.cards.special.Decryption;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：加密
 */
public class Encryption extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(Encryption.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public Encryption() {
        super(ID,
                false,
                cardStrings,
                0,
                CardType.SKILL,
                CardRarity.UNCOMMON,
                CardTarget.NONE,
                Ines.Enums.INES_CARD);
        this.magicNumber = this.baseMagicNumber = 1;
        this.exhaust = true;

        this.cardsToPreview = new Decryption();
    }
 
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new SelectHandCardAction(
                "加密",
                magicNumber,
                null,
                (selected) -> {
                    for (AbstractCard c : selected) {
                        // 放入抽牌堆顶
                        AbstractDungeon.player.hand.moveToDeck(c, false);
                        AbstractDungeon.player.hand.refreshHandLayout();

                        // 生成一张解密
                        Decryption decryption = new Decryption();
                        if (upgraded) {
                            decryption.upgrade();
                        }
                        decryption.setCardToDecryption(c);

                        addToTop(new MakeTempCardInHandAction(decryption,1));
                    }
                },
                true,
                true
        ));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.cardsToPreview.upgrade();

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
