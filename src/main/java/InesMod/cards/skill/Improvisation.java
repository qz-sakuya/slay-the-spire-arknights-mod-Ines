package InesMod.cards.skill;

import InesMod.cards.AbstractInesCard;
import InesMod.cards.special.AdHocStrategy;
import InesMod.cards.special.AdHocSupply;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.player.AdHocStrategyPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

/**
 * 中文卡名：应变
 */
public class Improvisation extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(Improvisation.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public Improvisation() {
        super(ID,
                false,
                cardStrings,
                0,
                CardType.SKILL,
                CardRarity.COMMON,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);

        this.cardToPreviewList.add(new AdHocStrategy());
        this.cardToPreviewList.add(new AdHocSupply());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> effectChoices = new ArrayList<>();
        effectChoices.add(new AdHocStrategy());
        effectChoices.add(new AdHocSupply());


        if (this.upgraded) {
            for (AbstractCard c : effectChoices) {
                c.upgrade();
            }
        }

        addToBot(new ChooseOneAction(effectChoices));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();

            for (AbstractCard c : this.cardToPreviewList) {
                c.upgrade();
            };

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
