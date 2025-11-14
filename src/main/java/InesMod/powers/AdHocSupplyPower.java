package InesMod.powers;

import InesMod.action.AdHocSupplyAction;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：临时补给
 */
public class AdHocSupplyPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(AdHocSupplyPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public AdHocSupplyPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);

        this.priority = 7; // 排在 临时战略 前面
    }

    @Override
    public void onCardMove(AbstractCard c, CardGroup.CardGroupType groupType) {
        addToBot(new AdHocSupplyAction(owner, amount));
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.amount);
    }
}
