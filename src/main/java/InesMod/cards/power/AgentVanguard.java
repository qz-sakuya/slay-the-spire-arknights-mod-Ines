package InesMod.cards.power;

import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：情报官
 */
public class AgentVanguard extends AbstractInesCard {
    public static final String ID = ModHelper.nameToId(AgentVanguard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public AgentVanguard() {
        super(ID,
                true,
                cardStrings,
                2,
                CardType.POWER,
                CardRarity.UNCOMMON,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //TODO
    }

    @Override
    public void upgrade() {
        upgradeName();
        upgradeBaseCost(1);
    }
}
