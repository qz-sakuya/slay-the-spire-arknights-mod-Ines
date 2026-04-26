package InesMod.cards.special;

import InesMod.action.ForceWaitAction;
import InesMod.action.TakeTurnAction;
import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.PathHelper;
import InesMod.monsters.Chapter10.Manfred;
import InesMod.powers.monster.MilitaryTrainingPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 中文卡名：回击
 * 衍生无色牌
 */
public class Counter extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(Counter.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public AbstractCard cardToDecryption = null;

    public Counter() {
        super(ID,
                false,
                cardStrings,
                0,
                CardType.ATTACK,
                CardRarity.SPECIAL,
                CardTarget.ENEMY,
                CardColor.COLORLESS);
        this.damage = this.baseDamage = 0;

        this.exhaust = true;

    }
 
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

//        if (upgraded) {
//            addToBot(new DrawCardAction(p, 1));
//        }

        if(bossCanMove()){
            for(AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                if(mo instanceof Manfred && !mo.isDeadOrEscaped()) {
                    addToBot(new ForceWaitAction(0.5F));
                    addToBot(new TakeTurnAction(mo));
                }
            }
        }
    }


    private boolean bossCanMove() {
        for(AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if(mo instanceof Manfred && !mo.isDeadOrEscaped()) {
                AbstractPower mtPower = mo.getPower(MilitaryTrainingPower.ID);
                if (mtPower instanceof MilitaryTrainingPower
                        && ((MilitaryTrainingPower)mtPower).invalidTurn == 0) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void applyPowers() {
        super.applyPowers();

        if (!upgraded) {
            this.rawDescription = cardStrings.DESCRIPTION;
        }
        else {
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        }

        // 添加额外文本
        if (bossCanMove()){
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
        }


        initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.selfRetain = true;

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
