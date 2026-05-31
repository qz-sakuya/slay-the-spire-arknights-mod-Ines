package InesMod.action;

import InesMod.helpers.LogHelper;
import InesMod.monsters.Chapter10.Manfred;
import InesMod.monsters.Chapter10.Teekazwurtzen;
import InesMod.powers.monster.FirePower;
import InesMod.powers.monster.MilitaryTrainingPower;
import InesMod.vfx.DefenseArtilleryFireEffect;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import java.util.ArrayList;

/**
 * 炮击 的伤害效果
 */
public class FireDamageAction extends AbstractGameAction {
    FirePower firePower;

    public FireDamageAction(FirePower firePower) {
        this.firePower = firePower;
    }





    @Override
    public void update() {
        LogHelper.info("===FireDamageAction：触发===");

        ArrayList<AbstractMonster> monsterArrayList = AbstractDungeon.getCurrRoom().monsters.monsters;

        firePower.spreadToNewMonster = false;

        // 获取中心坐标
        float centerX = (float) Settings.WIDTH / 2.0F;
        float centerY = (float) Settings.HEIGHT / 2.0F;


        //爆炸特效
        addToBot(new VFXAction(new DefenseArtilleryFireEffect(centerX * 0.85F, centerY * 0.6F)));
        addToBot(new ForceWaitAction(0.1F));
        addToBot(new VFXAction(new BorderFlashEffect(Color.ORANGE)));
        addToBot(new SFXAction("BLUNT_HEAVY", 0.2F));
        addToBot(new SFXAction("ATTACK_FIRE", 0.2F));



        // 对所有敌方造成伤害（固定伤害，参考爆炸机）
        int[] tmp = new int[monsterArrayList.size()];
        for (int i = 0; i < tmp.length; i++) {
            if (monsterArrayList.get(i) instanceof Teekazwurtzen) {
                tmp[i] = 0; // 不对提卡兹之根造成伤害
            }
            else{
                tmp[i] = firePower.damage;
            }
        }
        addToBot(new DamageAllEnemiesAction(firePower.owner, tmp, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));

        // 对玩家造成伤害
        addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(firePower.owner, firePower.damage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE));



        // 使曼弗雷德军事训练power失效
        for (AbstractMonster mo : monsterArrayList) {
            if (mo instanceof Manfred) {
                AbstractPower powerToGet = mo.getPower(MilitaryTrainingPower.ID);
                if (powerToGet != null) {
                    ((MilitaryTrainingPower)powerToGet).invalid();
                }
            }
        }


        // 删除所有怪物的power
        for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
            addToBot(new RemoveSpecificPowerAction(mon, mon, FirePower.ID));
        }

        // 删除玩家的power
        addToBot(new RemoveSpecificPowerAction(firePower.owner, firePower.owner, FirePower.ID));




        // 尝试清除特效
        addToBot(new TryClearDefenseArtilleryMeterUponAction(firePower.owner));

        this.isDone = true;
    }

}
