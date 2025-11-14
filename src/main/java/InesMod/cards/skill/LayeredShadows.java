package InesMod.cards.skill;

import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
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
 * 中文卡名：叠影
 */
public class LayeredShadows extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(LayeredShadows.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public LayeredShadows() {
        super(ID,
                false,
                cardStrings,
                0,
                CardType.SKILL,
                CardRarity.COMMON,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);

        this.cardsToPreview = new ShadowWhistle();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int cnt = 0;
        // 遍历手牌
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.cardID.equals(ShadowWhistle.ID)) {
                cnt++;
            }
        }
        this.addToBot(new MakeTempCardInHandAction(new ShadowWhistle(), cnt));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.selfRetain = true;

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
