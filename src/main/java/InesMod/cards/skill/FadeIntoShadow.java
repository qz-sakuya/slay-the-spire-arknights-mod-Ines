package InesMod.cards.skill;

import InesMod.action.AutoUseAction;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.modcore.InesModMain;
import InesMod.powers.InvisibilityPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.FreeAttackPower;

/**
 * 中文卡名：遁入影中
 */
public class FadeIntoShadow extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(FadeIntoShadow.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public FadeIntoShadow() {
        super(ID,
                false,
                cardStrings,
                1,
                CardType.SKILL,
                CardRarity.UNCOMMON,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
        this.magicNumber = baseMagicNumber = 2;

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new InvisibilityPower(p, 1), 1));

        // 结束你的回合
        addToBot(new PressEndTurnButtonAction());
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
        }
    }
}
