package InesMod.cards.attack;

import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.action.InesAttackAnimateAction;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

/**
 * 中文卡名：锉刀
 */
public class File extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(File.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public File() {
        super(ID,
                false,
                cardStrings,
                1,
                CardType.ATTACK,
                CardRarity.COMMON,
                CardTarget.ENEMY,
                Ines.Enums.INES_CARD);
        this.damage = this.baseDamage = 8;
        this.magicNumber = this.baseMagicNumber = 2;

        this.dontUseAttackAnimation = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        this.addToBot(new InesAttackAnimateAction(p, 1));
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

//        this.addToBot(new FileAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), this.magicNumber));

    }

    // 重写此函数，以支持格挡增伤
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.applyPowersToBlock();
        AbstractPlayer player = AbstractDungeon.player;
        this.isDamageModified = false;
        if (mo != null) {
            float tmp = (float)this.baseDamage;

            for(AbstractRelic r : player.relics) {
                tmp = r.atDamageModify(tmp, this);
                if (this.baseDamage != (int)tmp) {
                    this.isDamageModified = true;
                }
            }

            for(AbstractPower p : player.powers) {
                tmp = p.atDamageGive(tmp, this.damageTypeForTurn, this);
            }

            tmp = player.stance.atDamageGive(tmp, this.damageTypeForTurn, this);
            if (this.baseDamage != (int)tmp) {
                this.isDamageModified = true;
            }

            for(AbstractPower p : mo.powers) {
                tmp = p.atDamageReceive(tmp, this.damageTypeForTurn, this);
            }


            // 格挡增伤
            if (mo.currentBlock > 0) {
                tmp *= this.magicNumber;
            }


            for(AbstractPower p : player.powers) {
                tmp = p.atDamageFinalGive(tmp, this.damageTypeForTurn, this);
            }

            for(AbstractPower p : mo.powers) {
                tmp = p.atDamageFinalReceive(tmp, this.damageTypeForTurn, this);
            }

            if (tmp < 0.0F) {
                tmp = 0.0F;
            }

            if (this.baseDamage != MathUtils.floor(tmp)) {
                this.isDamageModified = true;
            }

            this.damage = MathUtils.floor(tmp);
        }

    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.upgradeDamage(1);
        }
    }
}
