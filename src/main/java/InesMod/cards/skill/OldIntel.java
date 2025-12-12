package InesMod.cards.skill;

import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.characters.Ines;
import InesMod.helpers.LoggerHelper;
import InesMod.helpers.PathHelper;
import InesMod.modcore.InesModMain;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
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
                true,
                cardStrings,
                0,
                CardType.SKILL,
                CardRarity.COMMON,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
        this.magicNumber = this.baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        LoggerHelper.info("===OldIntel：use：baseMagicNumber：{}===",this.baseMagicNumber);
        addToBot(new DrawCardAction(p, this.magicNumber));
        addToBot(new DiscardAction(p, p, 1, false));

        if(this.upgraded){

            this.upgradeMagicNumber(-1);

            if (this.baseMagicNumber < 0) {
                this.baseMagicNumber = 0;
            }
            LoggerHelper.info("===OldIntel：use：baseMagicNumber-1,当前值:：{}===",this.baseMagicNumber);
        }
    }

    // 有bug一回合触发两次onMoveToDiscard，但是bug又不能稳定复现，后面再出现再修吧
    @Override
    public void onMoveToDiscard() {
        LoggerHelper.info("===OldIntel：onMoveToDiscard：baseMagicNumber：{}===",this.baseMagicNumber);
        if(!this.upgraded){

            this.upgradeMagicNumber(-1);

            if (this.baseMagicNumber < 0) {
                this.baseMagicNumber = 0;
            }
            LoggerHelper.info("===OldIntel：onMoveToDiscard：baseMagicNumber-1,当前值:：{}===",this.baseMagicNumber);
        }
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
