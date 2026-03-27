package InesMod.powers.monster;

import InesMod.action.ForceWaitAction;
import InesMod.action.TryAddFirePowerAction;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.monsters.Chapter10.Manfred;
import InesMod.monsters.Chapter10.Teekazwurtzen;
import InesMod.powers.AbstractInesPower;
import InesMod.powers.player.InsightPower;
import InesMod.vfx.DefenseArtilleryFireEffect;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
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
 * 敌方power
 * 图标：原版进度条红色三角
 * 敌方回合结束时造成伤害
 */
public class FirePower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(FirePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源



    public FirePower(AbstractCreature owner, int amount) {
        super(ID,
                true,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount); // 一般为40伤

        this.priority = 0; // 倒数第二左，排在 城防炮充能 右侧


    }


    @Override
    public void atEndOfTurn(boolean isPlayer) {
        LogHelper.info("===FirePower atEndOfTurn：是否玩家回合：{}===",isPlayer);
        if (isPlayer) {
            Work();
        }
    }


    @Override
    public void atStartOfTurn(){
        Work();
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
                tmp[i] = this.amount;
            }
        }
        addToBot(new DamageAllEnemiesAction(this.owner, tmp, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));

        // 对玩家造成伤害
        addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE));


        addToBot(new ReducePowerAction(owner, owner, FirePower.ID, amount));
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.amount);
    }
}
