package InesMod.cards.attack;

import InesMod.vfx.ColouredSlashEffect;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.modcore.InesModMain;
import InesMod.powers.player.StealsPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;

/**
 * 中文卡名：影之束缚
 */
public class ShadowBind extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(ShadowBind.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public ShadowBind() {
        super(ID,
                false,
                cardStrings,
                2,
                CardType.ATTACK,
                CardRarity.UNCOMMON,
                CardTarget.ENEMY,
                Ines.Enums.INES_CARD);
        this.damage = this.baseDamage = 6;

        this.consumeSteals = 999;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new SFXAction("ATTACK_WHIFF_1", 0.2F));
        this.addToBot(new SFXAction("ATTACK_FAST", 0.2F));
        this.addToBot(new ColouredSlashEffect(m,135.0F, 3.0F,InesModMain.MY_COLOR_DARK,InesModMain.MY_COLOR));

        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageType.NORMAL), AbstractGameAction.AttackEffect.NONE));


        this.addToBot(new SFXAction("ATTACK_WHIFF_1", 0.2F));
        this.addToBot(new SFXAction("ATTACK_FAST", 0.2F));
        this.addToBot(new ColouredSlashEffect(m,45.0F, 3.0F,InesModMain.MY_COLOR_DARK,InesModMain.MY_COLOR));

        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageType.NORMAL), AbstractGameAction.AttackEffect.NONE));


        int getStealsNum = 0;
        AbstractPower stealsPower = p.getPower(StealsPower.ID);
        if (stealsPower != null) {
            getStealsNum = stealsPower.amount;
        }
        addToBot(new ApplyPowerAction(m, p, new WeakPower(p, getStealsNum,false), getStealsNum));
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
        }
    }
}
