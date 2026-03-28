package InesMod.action;

import InesMod.helpers.LogHelper;
import InesMod.powers.monster.DefenseArtilleryMeterPower;
import InesMod.powers.monster.FirePower;
import InesMod.vfx.DefenseArtilleryMeterUponManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 城防炮充能 判定是否添加 炮击！ 的 action
 */
public class SetDefenseArtilleryMeterUponAction extends AbstractGameAction {
    private final int type;
    public SetDefenseArtilleryMeterUponAction(int type) {
        this.type = type;
    }

    public void update() {
        DefenseArtilleryMeterUponManager.setEffect(type);

        this.isDone = true;
    }
}
