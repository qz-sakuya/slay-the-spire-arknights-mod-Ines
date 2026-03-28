package InesMod.action;

import InesMod.monsters.Chapter10.Teekazwurtzen;
import InesMod.powers.monster.DefenseArtilleryMeterPower;
import InesMod.powers.monster.FirePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 城防炮充能 判定是否添加 炮击！ 的 action
 */
public class TryAddFirePowerAction extends AbstractGameAction {
    private int damage;
    public TryAddFirePowerAction(AbstractCreature source, int damage) {
        this.source = source;
        this.damage = damage;
    }

    public void update() {
        AbstractPower powerToGet = source.getPower(DefenseArtilleryMeterPower.ID);
        if (powerToGet instanceof DefenseArtilleryMeterPower) {
            if (powerToGet.amount == ((DefenseArtilleryMeterPower) powerToGet).secondAmount) {
                
                
                // 给玩家添加power
                addToBot(new ApplyNonStackPowerAction(AbstractDungeon.player, source, new FirePower(AbstractDungeon.player, -1, damage)));


                // 给所有怪物添加power
                for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
                    if (mon instanceof Teekazwurtzen){
                        continue; // 跳过提卡兹之根
                    }

                    addToBot(new ApplyNonStackPowerAction(mon, source, new FirePower(mon, -1, damage)));
                }
            
            
            
            }
        }


        this.isDone = true;
    }
}
