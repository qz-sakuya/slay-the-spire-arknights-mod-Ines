package InesMod.cards.skill;

import InesMod.action.ApplyNonStackPowerAction;
import InesMod.action.CriticalInfoAction;
import InesMod.action.SelectPileCardAction;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.InformantPower;
import InesMod.powers.MercenaryTacticsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

/**
 * 中文卡名：线人
 */
public class Informant extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(Informant.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public Informant() {
        super(ID,
                false,
                cardStrings,
                0,
                CardType.SKILL,
                CardRarity.COMMON,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
        this.magicNumber = this.baseMagicNumber = 1;

    }
 
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> cardInInformantPower = new ArrayList<>();
        AbstractPower powerToGet = p.getPower(InformantPower.ID);
        if (powerToGet != null) {
            cardInInformantPower = ((InformantPower)powerToGet).cards;
        }


        ArrayList<AbstractCard> finalCardInInformantPower = cardInInformantPower;
        addToBot(new SelectPileCardAction(
                AbstractDungeon.player.drawPile.group,
                "在下回合检索并加入手牌",
                magicNumber,
                c -> !finalCardInInformantPower.contains(c),
                (selected) -> {
                    AbstractPower informantPower = p.getPower(InformantPower.ID);
                    if (informantPower != null) {
                        ((InformantPower)informantPower).addCards(selected);
                    }
                    else{
                        addToBot(new ApplyNonStackPowerAction(p, p, new InformantPower(p,-1,selected)));
                    }
                },
                true,
                true
        ));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
