package InesMod.cards.attack;

import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.ModHelper;
import InesMod.modcore.InesModMain;
import basemod.interfaces.OnCardUseSubscriber;
import basemod.interfaces.PostDrawSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

/**
 * 中文卡名：绝密行动
 *
 */
public class TopSecretOperation extends AbstractInesCard {
    public static final String ID = ModHelper.nameToId(TopSecretOperation.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public boolean successPlay = false;

    public TopSecretOperation() {
        super(ID,
                true,
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
        this.successPlay = true;

        for (int i = 0; i<2; i++){
            addToBot(new SFXAction("ATTACK_HEAVY"));
            addToBot(new VFXAction(p, new CleaveEffect(), 0.08F));
            addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
        }
    }

    // 用于外部调用
    public void autoUse() {
        InesModMain.logger.info("===TopSecretOperation-autoUse===");
        this.successPlay = false;
        this.applyPowers();

        if (!this.hasEnoughEnergy() || !this.cardPlayable(null)){
            // 无法打出则消耗
            addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
        }
        else {
            // 消耗能量打出
            this.addToBot(new LoseEnergyAction(this.cost));
            addToTop(new NewQueueCardAction(this, true, true, true));
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
            this.upgradeDamage(2);
        }
    }


    // TODO：patch moveToHand函数，使得加入手牌时也触发效果。还有查阅攻击药水的实现
}
