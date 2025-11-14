package InesMod.powers;

import InesMod.action.InterAction;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：情报
 */
public class InterPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(InterPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public InterPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
    }

    @Override
    public void onCardDraw(AbstractCard card){
        addToBot(new InterAction((AbstractPlayer) owner, 1));
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.amount);
    }
}
