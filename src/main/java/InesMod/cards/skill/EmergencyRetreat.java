package InesMod.cards.skill;

import InesMod.action.DelayToAddAction;
import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：紧急撤退
 */
public class EmergencyRetreat extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(EmergencyRetreat.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public EmergencyRetreat() {
        super(ID,
                false,
                cardStrings,
                1,
                CardType.SKILL,
                CardRarity.COMMON,
                CardTarget.NONE,
                Ines.Enums.INES_CARD);
        this.magicNumber = this.baseMagicNumber = 3;

        this.exhaust = true;
        this.isEthereal = true;

        this.cardsToPreview = new ShadowWhistle();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 丢弃所有手牌
        addToBot(new DiscardAction(p, p,99,false));

        // 生成3张影哨
        // 延迟是为了触发 临时补给 和 临时战略
        addToBot(new DelayToAddAction(new MakeTempCardInHandAction(new ShadowWhistle(), magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.exhaust = false;

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
