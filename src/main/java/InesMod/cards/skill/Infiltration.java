package InesMod.cards.skill;

import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.characters.Ines;
import InesMod.helpers.ModHelper;
import InesMod.powers.StealsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：渗透
 */
public class Infiltration extends AbstractInesCard {
    public static final String ID = ModHelper.nameToId(Infiltration.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public Infiltration() {
        super(ID,
                false,
                cardStrings,
                0,
                CardType.SKILL,
                CardRarity.COMMON,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = 0;
        for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
            if (!mon.isDeadOrEscaped()) {
                count++;
            }
        }
        if (this.upgraded) {
            count++;
        }
        this.addToBot(new ApplyPowerAction(p, p, new StealsPower(p, count), count));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
