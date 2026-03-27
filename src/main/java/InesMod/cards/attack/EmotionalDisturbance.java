package InesMod.cards.attack;

import InesMod.action.EmotionalDisturbanceAction;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.vfx.InesAttackEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：情绪干扰
 */
public class EmotionalDisturbance extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(EmotionalDisturbance.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源



    public EmotionalDisturbance() {
        super(ID,
                false,
                cardStrings,
                1,
                CardType.ATTACK,
                CardRarity.COMMON,
                CardTarget.ENEMY,
                Ines.Enums.INES_CARD);
        this.damage = this.baseDamage = 6;

        this.magicNumber = this.baseMagicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot(new EmotionalDisturbanceAction(p, m, magicNumber));
    }

//    @Override
//    public void triggerOnGlowCheck() {
//        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
//        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
//            if (!m.isDeadOrEscaped() && m.getIntentBaseDmg() < 0) {
//                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
//                break;
//            }
//        }
//    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);

        }
    }
}
