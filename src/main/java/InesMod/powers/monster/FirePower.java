package InesMod.powers.monster;

import InesMod.action.*;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.monsters.Chapter10.Manfred;
import InesMod.monsters.Chapter10.Teekazwurtzen;
import InesMod.powers.AbstractInesPower;
import InesMod.vfx.DefenseArtilleryFireEffect;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import java.util.ArrayList;

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
    public boolean spreadToNewMonster = true;

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
        if (spreadToNewMonster && this.owner instanceof AbstractPlayer) {
            addToBot(new ApplyNonStackPowerAction(mon, mon, new FirePower(mon, -1, damage)));
        }
    }



    // 在玩家回合结束时，处理伤害逻辑
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot(new DelayToAddAction(new FireDamageAction(this)));
        }
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
