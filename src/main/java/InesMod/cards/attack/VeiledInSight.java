package InesMod.cards.attack;

import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.InvisibilityPower;
import InesMod.powers.NoAttackPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：隐于目光
 */
public class VeiledInSight extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(VeiledInSight.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public VeiledInSight() {
        super(ID,
                true,
                cardStrings,
                2,
                CardType.ATTACK,
                CardRarity.UNCOMMON,
                CardTarget.ALL_ENEMY,
                Ines.Enums.INES_CARD);
        this.baseDamage = 9;
        this.isMultiDamage = true;

        this.selfRetain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new ApplyPowerAction(p, p, new InvisibilityPower(p, 1), 1));
        addToBot(new ApplyPowerAction(p, p, new NoAttackPower(p,-1), -1));
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
        }
    }
}
