package InesMod.powers.player;

import InesMod.action.RapidRedeploymentAction;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：快速重部署
 */
public class RapidRedeploymentPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(RapidRedeploymentPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public RapidRedeploymentPower(AbstractCreature owner, int amount) {
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
        addToBot(new ReducePowerAction(this.owner, this.owner, RapidRedeploymentPower.ID, this.amount));
        addToBot(new DrawCardAction(this.amount, new RapidRedeploymentAction()));
    }
}
