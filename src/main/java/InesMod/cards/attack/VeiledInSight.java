package InesMod.cards.attack;

import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.characters.Ines;
import InesMod.helpers.ModHelper;
import InesMod.powers.InvisibilityPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EntanglePower;
import com.megacrit.cardcrawl.vfx.combat.EntangleEffect;

/**
 * 中文卡名：隐于目光
 */
public class VeiledInSight extends AbstractInesCard {
    public static final String ID = ModHelper.nameToId(VeiledInSight.class.getSimpleName());
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
        this.cardsToPreview = new ShadowWhistle();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new ApplyPowerAction(p, p, new InvisibilityPower(p, 1), 1));
        addToBot(new ApplyPowerAction(p, p, new EntanglePower(p), 1));// TODO:写个独立power与不能出牌的文本框
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
        }
    }
}
