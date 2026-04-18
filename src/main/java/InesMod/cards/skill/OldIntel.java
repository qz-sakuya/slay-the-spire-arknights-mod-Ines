package InesMod.cards.skill;

import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：旧情报
 */
public class OldIntel extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(OldIntel.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public OldIntel() {
        super(ID,
                false,
                cardStrings,
                0,
                CardType.SKILL,
                CardRarity.COMMON,
                CardTarget.NONE,
                Ines.Enums.INES_CARD);
        this.magicNumber = this.baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        LogHelper.info("===OldIntel：use：baseMagicNumber：{}===",this.baseMagicNumber);
        addToBot(new DrawCardAction(p, this.magicNumber));
        addToBot(new DiscardAction(p, p, 1, false));

//        if(this.upgraded){
//
//            this.upgradeMagicNumber(-1);
//
//            if (this.baseMagicNumber < 0) {
//                this.baseMagicNumber = 0;
//            }
//            LogHelper.info("===OldIntel：use：baseMagicNumber-1,当前值:：{}===",this.baseMagicNumber);
//        }
    }

    // 有bug一回合触发两次onMoveToDiscard
    // 不能使用onMoveToDiscard否则看弃牌堆就计数
    @Override
    public void onCardMove(AbstractCard c, CardGroup.CardGroupType groupType) {
        // LogHelper.info("===OldIntel：onCardMove：begin");
        if (c == this
                && groupType == CardGroup.CardGroupType.DISCARD_PILE
                && !this.addedFromSameGroup) {
//            if(!this.upgraded){
//                // LogHelper.info("===OldIntel：onCardMove进入弃牌堆：baseMagicNumber：{}===",this.baseMagicNumber);
//                this.upgradeMagicNumber(-1);
//
//                if (this.baseMagicNumber < 0) {
//                    this.baseMagicNumber = 0;
//                }
//                // LogHelper.info("===OldIntel：onCardMove进入弃牌堆：baseMagicNumber-1,当前值:：{}===",this.baseMagicNumber);
//            }

            this.upgradeMagicNumber(-1);

            if (this.baseMagicNumber < 0) {
                this.baseMagicNumber = 0;
            }
        }
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);

//            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
//            this.initializeDescription();
        }
    }
}
