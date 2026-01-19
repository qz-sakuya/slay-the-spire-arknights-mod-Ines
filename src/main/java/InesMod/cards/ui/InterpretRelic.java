package InesMod.cards.ui;

import InesMod.cards.AbstractInesUICard;
import InesMod.helpers.PathHelper;
import InesMod.truth.TruthManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：解读真相：遗物
 */
public class InterpretRelic extends AbstractInesUICard {
    public static final String ID = PathHelper.nameToId(InterpretRelic.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public InterpretRelic() {
        super(ID,
                false,
                cardStrings);
        this.magicNumber = this.baseMagicNumber = 8;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public void upgrade() {}

    // 检测是否满足条件
    @Override
    public void JudgeAvailability() {
        available = TruthManager.getTotalAmount() >= this.magicNumber;
        this.rawDescription = cardStrings.DESCRIPTION;
        if (!available) {
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
        }
        this.initializeDescription();
    }
}
