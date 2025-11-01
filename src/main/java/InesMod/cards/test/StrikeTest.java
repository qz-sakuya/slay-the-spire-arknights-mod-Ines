package InesMod.cards.test;

import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.ModHelper;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.cards.AbstractCard;

/**
 * 中文卡名：打击测试
 * 只用于测试特效，正式版不注册
 */

@AutoAdd.Ignore
public class StrikeTest extends AbstractInesCard {
    public static final String ID = ModHelper.nameToId(StrikeTest.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // 当前特效索引
    private static int testEffectNum = 0;

    private static final AbstractGameAction.AttackEffect[] EFFECTS = {
            // 钝击，轻重好像只有声音区别
            AbstractGameAction.AttackEffect.BLUNT_LIGHT,
            AbstractGameAction.AttackEffect.BLUNT_HEAVY,

            // 斜劈，左上到右下， 轻重好像只有特效大小的微微区别，一般用轻的
            AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
            AbstractGameAction.AttackEffect.SMASH,

            // 重斩，左到右上
            AbstractGameAction.AttackEffect.SLASH_HEAVY,

            // 横轻斩
            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,

            // 竖轻斩
            AbstractGameAction.AttackEffect.SLASH_VERTICAL,

            // 无特效
            AbstractGameAction.AttackEffect.NONE,

            // 挂火
            AbstractGameAction.AttackEffect.FIRE,

            // 挂毒
            AbstractGameAction.AttackEffect.POISON,

            // 给目标挂盾（也许用于给友军挂盾？但为什么是攻击事件？）
            AbstractGameAction.AttackEffect.SHIELD,

            // 实际上是斜劈，这个特效大概没做
            AbstractGameAction.AttackEffect.LIGHTNING


            // 还有一些牌有专属特效，例如爪击、刮削、流云飞袖
    };

    public StrikeTest() {
        super(ID,
                true,
                cardStrings,
                1,
                AbstractCard.CardType.ATTACK,
                AbstractCard.CardRarity.BASIC,
                AbstractCard.CardTarget.ENEMY,
                Ines.Enums.INES_CARD);
        this.damage = this.baseDamage = 6;

        this.tags.add(AbstractCard.CardTags.STARTER_STRIKE);
        this.tags.add(AbstractCard.CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获取当前特效
        AbstractGameAction.AttackEffect effect = EFFECTS[testEffectNum];

        this.addToBot(new DamageAction(m, new DamageInfo(p, 0, DamageInfo.DamageType.NORMAL), effect));

        testEffectNum = (testEffectNum + 1) % EFFECTS.length;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
        }
    }
}