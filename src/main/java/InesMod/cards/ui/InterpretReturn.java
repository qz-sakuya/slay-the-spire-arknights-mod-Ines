package InesMod.cards.ui;

import InesMod.cards.AbstractInesUICard;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：解读真相：返回
 */
public class InterpretReturn extends AbstractInesUICard {
    public static final String ID = PathHelper.nameToId(InterpretReturn.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public InterpretReturn() {
        super(ID,
                false,
                cardStrings);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public void upgrade() {}
}
