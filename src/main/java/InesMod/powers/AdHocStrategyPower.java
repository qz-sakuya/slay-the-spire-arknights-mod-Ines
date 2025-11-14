package InesMod.powers;

import InesMod.action.AdHocStrategyAction;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：临时战略
 */
public class AdHocStrategyPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(AdHocStrategyPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public AdHocStrategyPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);

        this.priority = 8; // 排在 临时补给 后面
    }

    @Override
    public void onCardMove(AbstractCard c, CardGroup.CardGroupType groupType) {
        addToBot(new AdHocStrategyAction(owner, amount));
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.amount);
    }
}
