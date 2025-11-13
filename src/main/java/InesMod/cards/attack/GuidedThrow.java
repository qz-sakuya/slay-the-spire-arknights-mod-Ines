package InesMod.cards.attack;

import InesMod.action.FileAction;
import InesMod.action.GuidedThrowAction;
import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.characters.Ines;
import InesMod.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 中文卡名：牵引投掷
 */
public class GuidedThrow extends AbstractInesCard {
    public static final String ID = ModHelper.nameToId(GuidedThrow.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public GuidedThrow() {
        super(ID,
                true,
                cardStrings,
                1,
                CardType.ATTACK,
                CardRarity.UNCOMMON,
                CardTarget.ALL_ENEMY,
                Ines.Enums.INES_CARD);
        this.baseDamage = 8;
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
        addToBot(new GuidedThrowAction(p, this.multiDamage, this.damageTypeForTurn));
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
        }
    }
}
