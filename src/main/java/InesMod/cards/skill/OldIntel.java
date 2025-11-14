package InesMod.cards.skill;

import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.modcore.InesModMain;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
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
                1,
                CardType.SKILL,
                CardRarity.COMMON,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
        this.magicNumber = this.baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        InesModMain.logger.info("===OldIntel：use：baseMagicNumber：{}===",this.baseMagicNumber);
        addToBot(new DrawCardAction(p, this.magicNumber));
        addToBot(new DiscardAction(p, p, 1, false));

        if(this.upgraded){
            this.baseMagicNumber -= 1;
            if (this.baseMagicNumber < 0) {
                this.baseMagicNumber = 0;
            }
            InesModMain.logger.info("===OldIntel：use：baseMagicNumber-1,now:：{}===",this.baseMagicNumber);
        }
    }

    @Override
    public void onMoveToDiscard() {
        InesModMain.logger.info("===OldIntel：onMoveToDiscard：baseMagicNumber：{}===",this.baseMagicNumber);
        if(!this.upgraded){
            this.baseMagicNumber -= 1;
            if (this.baseMagicNumber < 0) {
                this.baseMagicNumber = 0;
            }
            InesModMain.logger.info("===OldIntel：onMoveToDiscard：baseMagicNumber-1,now:：{}===",this.baseMagicNumber);
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
