package InesMod.powers.player;

import InesMod.action.AdHocAction;
import InesMod.characters.Ines;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.patchs.InPlayerEndTurnPeriodManager;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
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
        if (!InPlayerEndTurnPeriodManager.inPlayerEndTurnPeriod) {
            LogHelper.info("===AdHocSupplyPower：onCardMove：尝试触发===");
            addToBot(new AdHocAction(owner));
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (!InPlayerEndTurnPeriodManager.inPlayerEndTurnPeriod) {
            LogHelper.info("===AdHocSupplyPower：onAfterUseCard：尝试触发===");
            addToBot(new AdHocAction(owner));
        }
    }

    @Override
    public void atStartOfTurn() {
        LogHelper.info("===AdHocSupplyPower：atStartOfTurn===");

        // 与 临时战略 保持一致
        addToTop(new AdHocAction(owner));
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
