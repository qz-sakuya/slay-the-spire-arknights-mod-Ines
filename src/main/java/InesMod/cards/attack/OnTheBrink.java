package InesMod.cards.attack;

import InesMod.action.ResetOnTheBrinkAction;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.vfx.OnTheBrinkEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：一触即发
 */
public class OnTheBrink extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(OnTheBrink.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public int initDamage = 8;
    int updateAmount = 2;

    public OnTheBrink() {
        super(ID,
                false,
                cardStrings,
                1,
                CardType.ATTACK,
                CardRarity.RARE,
                CardTarget.ENEMY,
                Ines.Enums.INES_CARD);
        this.damage = this.baseDamage = initDamage;
        this.magicNumber = this.baseMagicNumber = 0;


    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new VFXAction(new OnTheBrinkEffect(m.hb.cX, m.hb.cY,400f * Settings.scale), 0.4F));
        addToBot(new SFXAction("ATTACK_HEAVY"));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));

        // 重置攻击力
        addToBot(new ResetOnTheBrinkAction(this));
    }



    @Override
    public void atBattleStartPreDraw() {
        resetDamage();
    }

    @Override
    public void atBattleEnd() {
        resetDamage();
    }


    @Override
    public void onManualDiscard(AbstractCard c){
        tryAddDamage(c);
    }

    @Override
    public void onReceiveCardUsed(AbstractCard c) {
        tryAddDamage(c);
    }

    // 增加一次攻击
    private void tryAddDamage(AbstractCard c){
        if (c.type != AbstractCard.CardType.ATTACK) {
            upgradeDamage(updateAmount);

            this.baseMagicNumber += updateAmount;

            // 添加额外文本
            this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
            initializeDescription();
        }
    }

    // 重置额外攻击
    public void resetDamage() {
        this.damage = this.baseDamage = this.initDamage;
        this.isDamageModified = false;

        this.baseMagicNumber = 0;

        this.applyPowers();
    }


    @Override
    public AbstractCard makeStatEquivalentCopy() {
        OnTheBrink card = (OnTheBrink) super.makeStatEquivalentCopy();

        card.initDamage = this.initDamage;
        card.updateAmount = this.updateAmount;
        return card;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.updateAmount = 3;

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
