package InesMod.cards.skill;

import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.powers.StealsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：渗透
 */
public class Infiltration extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(Infiltration.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public Infiltration() {
        super(ID,
                false,
                cardStrings,
                0,
                CardType.SKILL,
                CardRarity.COMMON,
                CardTarget.ALL,// 自身与所有敌人
                Ines.Enums.INES_CARD);
        this.draw = 1;

    }
 
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = 0;
        for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
            if (!mon.isDeadOrEscaped()) {
                count++;
            }
        }
        // 升级额外+1
        if (this.upgraded) {
            count++;
        }
        this.addToBot(new ApplyPowerAction(p, p, new StealsPower(p, count), count));

        this.addToBot(new DrawCardAction(p, this.draw));
        this.addToBot(new MakeTempCardInDiscardAction(makeStatEquivalentCopy(), 1));
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
