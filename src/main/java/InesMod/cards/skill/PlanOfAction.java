package InesMod.cards.skill;

import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.StealsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：行动预谋
 */
public class PlanOfAction extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(PlanOfAction.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public PlanOfAction() {
        super(ID,
                true,
                cardStrings,
                1,
                CardType.SKILL,
                CardRarity.BASIC,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
        this.draw = 1;
        this.magicNumber = this.baseMagicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(p, this.draw));
        this.addToBot(new ApplyPowerAction(p, p, new StealsPower(p, this.magicNumber ), this.magicNumber));
        this.addToBot(new GainEnergyAction(1));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.draw = 2;
            this.upgradeMagicNumber(1);

            // 升级文本是为了预留两个值提升不一致的情况
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
