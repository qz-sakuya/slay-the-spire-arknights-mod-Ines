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
 * 中文卡名：人事部密信
 */
public class SecretHRLetter extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(SecretHRLetter.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public SecretHRLetter() {
        super(ID,
                false,
                cardStrings,
                3,
                CardType.SKILL,
                CardRarity.RARE,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 遍历手牌
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            c.setCostForTurn(c.costForTurn - 2); // 耗能-2
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeBaseCost(2);
        }
    }
}
