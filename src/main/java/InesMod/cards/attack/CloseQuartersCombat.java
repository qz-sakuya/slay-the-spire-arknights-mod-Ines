package InesMod.cards.attack;

import InesMod.action.CloseQuartersCombatAction;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.vfx.InesAttackEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：近身格斗
 */
public class CloseQuartersCombat extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(CloseQuartersCombat.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public CloseQuartersCombat() {
        super(ID,
                false,
                cardStrings,
                1,
                CardType.ATTACK,
                CardRarity.COMMON,
                CardTarget.ENEMY,
                Ines.Enums.INES_CARD);
        this.damage = this.baseDamage = 6;
        this.magicNumber = this.baseMagicNumber = 6;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 必须把两次攻击特效一个写里面一个写外面，不能都在action里addToTop，否则会有人物消失bug，原因未知
        addToBot(new InesAttackEffect(p,1));

        addToBot(new CloseQuartersCombatAction(p, m, damage, magicNumber));
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if(AbstractDungeon.player.currentBlock == 0){
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
        }
    }
}
