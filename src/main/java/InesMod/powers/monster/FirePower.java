package InesMod.powers.monster;

import InesMod.action.*;
import InesMod.helpers.PathHelper;
import InesMod.monsters.Chapter10.Teekazwurtzen;
import InesMod.powers.AbstractInesPower;
import InesMod.vfx.FireTipEffect;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.Iterator;

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

        addTipEffect();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        addTipEffect();
    }


    @Override
    public void onSpawnMonster(AbstractMonster mon){
        // 为新怪传播该power
        if (spreadToNewMonster
                && this.owner instanceof AbstractPlayer
                && !(mon instanceof Teekazwurtzen)) {
            addToBot(new ApplyNonStackPowerAction(mon, mon, new FirePower(mon, -1, damage)));
        }
    }



    // 在玩家回合结束时，处理伤害逻辑
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            stopTipEffect();
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




    private void addTipEffect(){
        if (!(this.owner instanceof AbstractPlayer)) {
            return;
        }

        ArrayList<AbstractGameEffect> effect = ReflectionHacks.getPrivate(this, AbstractPower.class, "effect");

        if (effect != null) {
            boolean hasSameType = false;
            for (AbstractGameEffect existingEffect : effect) {
                if (existingEffect instanceof FireTipEffect) {
                    hasSameType = true;
                    break;
                }
            }

            if (!hasSameType) {
                effect.add(new FireTipEffect());
            }
        }
    }

    private void stopTipEffect(){
        ArrayList<AbstractGameEffect> effect = ReflectionHacks.getPrivate(this, AbstractPower.class, "effect");

        if (effect != null) {
            for (AbstractGameEffect existingEffect : effect) {
                if (existingEffect instanceof FireTipEffect) {
                    ((FireTipEffect) existingEffect).stop = true;
                    break;
                }
            }
        }
    }
}
