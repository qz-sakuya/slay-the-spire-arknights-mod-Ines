package InesMod.cards.skill;

import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：徘徊
 */
public class Wander extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(Wander.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public Wander() {
        super(ID,
                false,
                cardStrings,
                1,
                CardType.SKILL,
                CardRarity.UNCOMMON,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
        this.block = this.baseBlock = 5;
        this.magicNumber = this.baseMagicNumber = 0;

        this.cardsToPreview = new ShadowWhistle();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        this.magicNumber = this.baseMagicNumber;
        for (int i = 0; i < this.magicNumber; ++i) {
            this.addToBot(new GainBlockAction(p, p, block));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        int cnt = 0;
        // 遍历手牌
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.cardID.equals(ShadowWhistle.ID)) {
                cnt++;
            }
        }
        // 遍历抽牌堆
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c.cardID.equals(ShadowWhistle.ID)) {
                cnt++;
            }
        }
        this.baseMagicNumber = cnt;

        // 添加额外文本
        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeBlock(2);
        }
    }
}
