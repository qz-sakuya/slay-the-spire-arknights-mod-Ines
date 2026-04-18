package InesMod.action;

import InesMod.helpers.LogHelper;
import InesMod.powers.monster.DefenseArtilleryMeterPower;
import InesMod.powers.monster.FirePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 曼弗雷德重生时，清零 城防炮充能 的效果
 */
public class ManfredClearDefenseArtilleryMeterPowerAction extends AbstractGameAction {
    DefenseArtilleryMeterPower power;
    
    public ManfredClearDefenseArtilleryMeterPowerAction(DefenseArtilleryMeterPower power) {
        this.power = power;
    }



    @Override
    public void update() {
        LogHelper.info("===ManfredClearDefenseArtilleryMeterPowerAction：触发===");

        // 如果有刚施加的炮击，不清空
        boolean hasFirePower = false;
        AbstractPower powerToGet = AbstractDungeon.player.getPower(FirePower.ID);
        if (powerToGet != null) {
            hasFirePower = true;
        }
        if (!hasFirePower) {
            addToBot(new SetPowerAction(power.owner, power.owner, new DefenseArtilleryMeterPower(power.owner, 0, power.secondAmount, power.damage), 0));
            power.notAddThisTurn = true;

            LogHelper.info("===ManfredClearDefenseArtilleryMeterPowerAction：成功清空充能===");
        }
        else{
            addToBot(new ApplyNonStackPowerAction(power.owner, power.owner, new FirePower(power.owner, -1,power.damage)));
        }
        this.isDone = true;
    }

}
