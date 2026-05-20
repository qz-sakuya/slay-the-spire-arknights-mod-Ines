package InesMod.cards.attack;

import InesMod.action.ForceWaitAction;
import InesMod.action.GuidedThrowAction;
import InesMod.action.InesAttackAnimateAction;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：牵引投掷
 */
public class GuidedThrow extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(GuidedThrow.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public GuidedThrow() {
        super(ID,
                false,
                cardStrings,
                1,
                CardType.ATTACK,
                CardRarity.UNCOMMON,
                CardTarget.ALL_ENEMY,
                Ines.Enums.INES_CARD);
        this.baseDamage = 9;
        this.isMultiDamage = true;

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = 0;
        for (AbstractMonster m2 : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (!m2.isDeadOrEscaped()) {
                count++;
            }
        }
        this.consumeSteals = count;

        this.addToBot(new ForceWaitAction(0.2F));
        this.addToBot(new GuidedThrowAction(p, this.multiDamage, this.damageTypeForTurn));
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
        }
    }
}
