package InesMod.powers;

import InesMod.action.AdHocSupplyAction;
import InesMod.characters.Ines;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：临时补给
 */
public class AdHocSupplyPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(AdHocSupplyPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public boolean inEndTurnPeriod; // 单独设置一个全局变量，仅尝试在构造函数与Ines类同步，增加通用性。

    public AdHocSupplyPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);

        this.priority = 7; // 排在 临时战略 前面

        if (AbstractDungeon.player instanceof Ines) {
            this.inEndTurnPeriod = ((Ines)AbstractDungeon.player).inEndTurnPeriod;
        }
        else {
            this.inEndTurnPeriod = false;
        }

    }

    @Override
    public void onCardMove(AbstractCard c, CardGroup.CardGroupType groupType) {
        if (!this.inEndTurnPeriod) {
            LogHelper.info("===AdHocSupplyPower：onCardMove：尝试触发===");
            addToBot(new AdHocSupplyAction(owner, amount));
        }
    }

    @Override
    public void atStartOfTurn() {
        LogHelper.info("===AdHocSupplyPower：atStartOfTurn===");
        this.inEndTurnPeriod = false;

        addToBot(new AdHocSupplyAction(owner, amount));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        LogHelper.info("===AdHocSupplyPower：atEndOfTurn===");
        if(isPlayer) {
            this.inEndTurnPeriod = true;
        }
    }

    @Override
    public void updateDescription() {
        StringBuilder energyText = new StringBuilder();
        for (int i = 0; i<this.amount; i++) {
            energyText.append(" [E] ");
        }
        this.description = String.format(descriptions[0], energyText);
    }
}
