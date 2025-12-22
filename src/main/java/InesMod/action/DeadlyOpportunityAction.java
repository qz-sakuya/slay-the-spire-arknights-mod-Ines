package InesMod.action;

import InesMod.helpers.LogHelper;
import InesMod.powers.DeadlyOpportunityPower;
import InesMod.powers.InterPower;
import InesMod.powers.ShadowWhistleRetrievalPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 致命契机 的效果
 */
public class DeadlyOpportunityAction extends AbstractGameAction {
    boolean isUpgrade;

    public DeadlyOpportunityAction(AbstractPlayer source, boolean isUpgrade) {
        this.source = source;
        this.isUpgrade = isUpgrade;
    }



    @Override
    public void update() {
        // 结束你的回合
        this.addToTop(new PressEndTurnButtonAction());

        if (!isUpgrade){
            this.addToTop(new RemoveAllBlockAction(source, source));
        }
        else{
            this.addToTop(new RemoveHalfBlockAction(source, source));
        }

        AbstractPower powerToGet = AbstractDungeon.player.getPower(DeadlyOpportunityPower.ID);
        if (powerToGet != null) {
            powerToGet.flash();
        }
        addToTop(new RemoveSpecificPowerAction(source, source, DeadlyOpportunityPower.ID));


        this.isDone = true;
    }

}
