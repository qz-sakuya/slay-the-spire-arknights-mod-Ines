package InesMod.cards.power;

import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.MercenaryTacticsPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.WeakPower;

/**
 * 中文卡名：王庭盟约
 */
public class RoyalAllianceTreaty extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(RoyalAllianceTreaty.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public RoyalAllianceTreaty() {
        super(ID,
                false,
                cardStrings,
                2,
                CardType.POWER,
                CardRarity.RARE,
                CardTarget.ALL, // 自身和所有敌人
                Ines.Enums.INES_CARD);
        this.magicNumber = this.baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new IntangiblePower(p, magicNumber), magicNumber));

        for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            addToBot(new ApplyPowerAction(mo, p, new IntangiblePower(mo, this.magicNumber), this.magicNumber, true));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.selfRetain = true;

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
