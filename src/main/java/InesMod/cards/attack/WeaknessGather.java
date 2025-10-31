package InesMod.cards.attack;

import InesMod.action.SetPowerAction;
import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.characters.Ines;
import InesMod.helpers.ModHelper;
import InesMod.powers.InvisibilityPower;
import InesMod.powers.StealsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 中文卡名：弱点收集
 */
public class WeaknessGather extends AbstractInesCard {
    public static final String ID = ModHelper.nameToId(WeaknessGather.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public WeaknessGather() {
        super(ID,
                true,
                cardStrings,
                1,
                CardType.ATTACK,
                CardRarity.BASIC,
                CardTarget.ENEMY,
                Ines.Enums.INES_CARD);
        this.damage = this.baseDamage = 4;
        this.magicNumber = this.baseMagicNumber = 1;

        this.cardsToPreview = new ShadowWhistle();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageType.NORMAL)));

        addToBot(new SetPowerAction(p, p, new StealsPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
            this.upgradeMagicNumber(1);
        }
    }
}
