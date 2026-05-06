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
        if (!InPlayerEndTurnPeriodManager.inPlayerEndTurnPeriod) {
            LogHelper.info("===AdHocStrategyPower：onCardMove：尝试触发===");
            addToBot(new AdHocAction(owner));
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (!InPlayerEndTurnPeriodManager.inPlayerEndTurnPeriod) {
            LogHelper.info("===AdHocStrategyPower：onAfterUseCard：尝试触发===");
            addToBot(new AdHocAction(owner));
        }
    }

    @Override
    public void atStartOfTurn() {
        LogHelper.info("===AdHocStrategyPower：atStartOfTurn===");

        // 优先于所有回合开始时塞牌
        addToTop(new AdHocAction(owner));
    }



    @Override
    public void updateDescription() {
        if (this.amount <= 1){
            this.description = String.format(descriptions[0], this.amount);
        }
        else {
            this.description = String.format(descriptions[1], this.amount); // 复数
        }
    }
}
