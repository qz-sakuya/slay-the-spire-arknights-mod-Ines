package InesMod.powers.monster;

import InesMod.action.ApplyNonStackPowerAction;
import InesMod.action.ForceWaitAction;
import InesMod.action.TryAddFirePowerAction;
import InesMod.action.TryClearDefenseArtilleryMeterUponAction;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.monsters.Chapter10.Manfred;
import InesMod.monsters.Chapter10.Teekazwurtzen;
import InesMod.powers.AbstractInesPower;
import InesMod.powers.player.InsightPower;
import InesMod.vfx.DefenseArtilleryFireEffect;
import InesMod.vfx.DefenseArtilleryMeterUponManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 中文名：炮击！
 * 英文名：Fire!
 * 敌人+玩家的power
 * 仅玩家身上的power处理伤害逻辑，怪物的仅是一个标识
 * 图标：原版进度条红色三角
 */
public class FirePower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(FirePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源


    public int damage; // 一般为40伤

    public FirePower(AbstractCreature owner, int amount, int damage) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount); // 不可叠加，不显示数字使图标更明显

        this.priority = 9999; // 排在最右侧

        this.damage = damage;
        updateDescription();
    }


    @Override
    public void onSpawnMonster(AbstractMonster mon){
        // 为新怪传播该power
        addToBot(new ApplyNonStackPowerAction(mon, mon, new FirePower(mon, -1, damage)));
    }



    // 在玩家回合结束时，处理伤害逻辑
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            Work();
        }
    }



    private void Work() {
        // 使曼弗雷德军事训练power失效
        ArrayList<AbstractMonster> monsterArrayList = AbstractDungeon.getCurrRoom().monsters.monsters;
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
        addToBot(new RemoveSpecificPowerAction(owner, owner, FirePower.ID));



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
                tmp[i] = this.damage;
            }
        }
        addToBot(new DamageAllEnemiesAction(this.owner, tmp, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));

        // 对玩家造成伤害
        addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(this.owner, this.damage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE));





        addToBot(new TryClearDefenseArtilleryMeterUponAction(this.owner));
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.damage);

//        if (this.owner == AbstractDungeon.player) {
//            this.description = String.format(descriptions[0], this.damage);
//        }
//        else {
//            this.description = String.format(descriptions[1], this.damage);
//        }
    }
}
