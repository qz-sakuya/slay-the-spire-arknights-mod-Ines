package InesMod.cards.skill;

import InesMod.action.RandomSearchCardAction;
import InesMod.action.SmartMoveToHandAction;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.player.StealsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

/**
 * 中文卡名：行动预谋
 */
public class PlanOfAction extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(PlanOfAction.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public PlanOfAction() {
        super(ID,
                false,
                cardStrings,
                1,
                CardType.SKILL,
                CardRarity.BASIC,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
        this.magicNumber = this.baseMagicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // this.addToBot(new DrawCardAction(p, magicNumber));


        addToBot(new RandomSearchCardAction(
                magicNumber,
                c -> c.type == CardType.ATTACK,
                (selected) -> {

                    addToTop(new SmartMoveToHandAction(new ArrayList<>(selected)));

                    AbstractDungeon.player.hand.refreshHandLayout();
                }
        ));

        this.addToBot(new ApplyPowerAction(p, p, new StealsPower(p, magicNumber), magicNumber));
        this.addToBot(new GainEnergyAction(1));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }
}
