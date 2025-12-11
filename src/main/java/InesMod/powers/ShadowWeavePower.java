package InesMod.powers;

import InesMod.action.RapidRedeploymentAction;
import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：影织
 */
public class ShadowWeavePower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(ShadowWeavePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public ShadowWeavePower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
    }


    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.amount);
    }

    @Override
    public void atStartOfTurn() {
        flash();
        this.addToBot(new MakeTempCardInHandAction(new ShadowWhistle(), this.amount));
    }
}
