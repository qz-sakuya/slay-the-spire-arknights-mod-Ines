package InesMod.cards.attack;

import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.action.InesAttackAnimateAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：淬影突袭
 */
public class ShadowAmbush extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(ShadowAmbush.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public ShadowAmbush() {
        super(ID,
                false,
                cardStrings,
                1,
                CardType.ATTACK,
                CardRarity.BASIC,
                CardTarget.ENEMY,
                Ines.Enums.INES_CARD);
        this.damage = this.baseDamage = 4;
        this.magicNumber = this.baseMagicNumber = 1;

        this.cardsToPreview = new ShadowWhistle();

        this.dontUseAttackAnimation = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {


        this.addToBot(new InesAttackAnimateAction(p, 2, 1.2F));
        // ((Ines) AbstractDungeon.player).playAttackAnimation(2);

        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        this.addToBot(new MakeTempCardInHandAction(new ShadowWhistle(), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(4);
        }
    }
}
