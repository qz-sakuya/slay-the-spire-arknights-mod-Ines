package InesMod.cards.attack;

import InesMod.action.AutoUseOrExhaustAction;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

/**
 * 中文卡名：绝密行动
 *
 */
public class TopSecretOperation extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(TopSecretOperation.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源



    public TopSecretOperation() {
        super(ID,
                false,
                cardStrings,
                2,
                CardType.ATTACK,
                CardRarity.RARE,
                CardTarget.ENEMY,
                Ines.Enums.INES_CARD);

        this.baseDamage = 12;
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i<2; i++){
            addToBot(new SFXAction("ATTACK_HEAVY"));
            addToBot(new VFXAction(p, new CleaveEffect(), 0.08F));
            addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
        }
    }


    // 对于 冥想 将此牌加入手牌，无法触发 自动打出 ，猜测是强制结束回合，新 NewQueueCardAction 没有触发
    @Override
    public void autoUse() {
        LogHelper.info("===TopSecretOperation-autoUse===");

        // 必须在 action 中判断能量与打出条件
        addToTop(new AutoUseOrExhaustAction(this));
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
            this.upgradeDamage(2);
        }
    }

}
