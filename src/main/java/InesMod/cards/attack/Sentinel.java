package InesMod.cards.attack;

import InesMod.action.ApplyNonStackPowerAction;
import InesMod.action.InesAttackAnimateAction;
import InesMod.action.SelectHandCardAction;
import InesMod.action.SelectPileCardAction;
import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.player.InformantPower;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

/**
 * 中文卡名：锉刀
 */
public class Sentinel extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(Sentinel.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public Sentinel() {
        super(ID,
                false,
                cardStrings,
                1,
                CardType.ATTACK,
                CardRarity.COMMON,
                CardTarget.ENEMY,
                Ines.Enums.INES_CARD);
        this.damage = this.baseDamage = 9;
        this.magicNumber = this.baseMagicNumber = 1;

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));

        this.addToBot(new SelectHandCardAction(
                "丢弃",
                1,
                null,
                (selected) -> {
                    for (AbstractCard c : selected) {
                        // 丢弃
                        AbstractDungeon.player.hand.moveToDiscardPile(c);

                        c.triggerOnManualDiscard();
                        GameActionManager.incrementDiscard(false);

                        // 若丢弃影哨，获得1张影哨
                        if (c instanceof ShadowWhistle) {
                            this.addToBot(new MakeTempCardInDrawPileAction(new ShadowWhistle(), this.magicNumber, true, true));
                        }
                    }

                    AbstractDungeon.player.hand.applyPowers();
                },
                true,
                false
        ));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);

        }
    }
}
