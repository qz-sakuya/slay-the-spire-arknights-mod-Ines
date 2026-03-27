package InesMod.cards.attack;

import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.vfx.InesAttackEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：出其不意
 */
public class CatchOffGuard extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(CatchOffGuard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public CatchOffGuard() {
        super(ID,
                false,
                cardStrings,
                0,
                CardType.ATTACK,
                CardRarity.COMMON,
                CardTarget.ENEMY,
                Ines.Enums.INES_CARD);
        this.damage = this.baseDamage = 10;
        this.magicNumber = this.baseMagicNumber = 1;

        this.consumeSteals = 0;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.consumeSteals = 0;

        // 刚打出去，所以列表中有自己
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == 1 && AbstractDungeon.actionManager.cardsPlayedThisTurn.get(0) == this) {
            this.consumeSteals = 1;
            this.addToBot(new InesAttackEffect(p, 3));
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            this.addToBot(new DrawCardAction(p, this.magicNumber));
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if(AbstractDungeon.actionManager.cardsPlayedThisTurn.isEmpty()){
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }
}
