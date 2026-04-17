package InesMod.action;

import InesMod.powers.monster.DefenseArtilleryMeterPower;
import InesMod.powers.monster.FirePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 尝试清零 城防炮充能 的效果
 */
public class TryClearDefenseArtilleryMeterPowerAction extends AbstractGameAction {
    DefenseArtilleryMeterPower power;

    public TryClearDefenseArtilleryMeterPowerAction(DefenseArtilleryMeterPower power) {
        this.power = power;
    }



    @Override
    public void update() {
        // 如果有本回合未能触发的炮击，不清空
        AbstractPower powerToGet = AbstractDungeon.player.getPower(FirePower.ID);
        if (powerToGet != null) {
            this.isDone = true;
            return;
        }


        if (power.amount >= power.secondAmount) {
            addToBot(new SetPowerAction(power.owner,power. owner, power, 0));
        }

        this.isDone = true;
    }

}
