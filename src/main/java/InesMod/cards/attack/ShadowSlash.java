package InesMod.cards.attack;

import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.characters.Ines;
import InesMod.helpers.ModHelper;
import InesMod.powers.AgentVanguardPower;
import InesMod.powers.InterPower;
import InesMod.powers.StealsPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 中文卡名：影斩
 */
public class ShadowSlash extends AbstractInesCard {
    public static final String ID = ModHelper.nameToId(ShadowSlash.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public ShadowSlash() {
        super(ID,
                false,
                cardStrings,
                2,
                CardType.ATTACK,
                CardRarity.RARE,
                CardTarget.ENEMY,
                Ines.Enums.INES_CARD);
        this.damage = this.baseDamage = 12;

        this.consumeSteals = 999;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HEAVY));

        int getStealsNum = 0;
        AbstractPower stealsPower = p.getPower(StealsPower.ID);
        if (stealsPower != null) {
            getStealsNum = stealsPower.amount;
        }
        addToBot(new ApplyPowerAction(p, p, new StealsPower(p, getStealsNum), getStealsNum));
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(6);
        }
    }
}
