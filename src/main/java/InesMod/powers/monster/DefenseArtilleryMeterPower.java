package InesMod.powers.monster;

import InesMod.action.*;
import InesMod.helpers.PathHelper;
import InesMod.monsters.Chapter10.Manfred;
import InesMod.powers.AbstractInesPower;
import InesMod.vfx.DefenseArtilleryMeterUponManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 中文名：城防炮充能
 * 英文名：Defense Artillery Meter
 * 敌方power
 * 图标：透视准心+斜向下箭头（划掉）
 * 图标：原版进度条黄色三角
 */
public class DefenseArtilleryMeterPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(DefenseArtilleryMeterPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public final int damage;

    public boolean notAddThisTurn = false;

    public DefenseArtilleryMeterPower(AbstractCreature owner, int amount, int secondAmount, int damage) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount,
                secondAmount);
        this.damage = damage;

        this.priority = 0; // 最左侧

        this.isTurnBased = true;
        this.renderAmountZero = true;

        DefenseArtilleryMeterUponManager.setEffect(amount);
    }


    @Override
    public void atStartOfTurn() {
        // 回合开始时才归零
        addToBot(new DelayToAddAction(new TryClearDefenseArtilleryMeterPowerAction(this)));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!notAddThisTurn && amount < secondAmount) {
            addToBot(new ApplyPowerAction(owner, owner, this,1));
        }
        addToBot(new TryAddFirePowerAction(owner, damage));

        notAddThisTurn = false;
    }



//    @Override // 重写，使得层数为0时也可以绘制数字
//    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
//        super.renderAmount(sb, x, y, c);
//
//        if (this.amount >= 0) {
//            // 默认白色
//            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
//        }
//    }

    // 重写，以更新动画
    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        addToTop(new SetDefenseArtilleryMeterUponAction(amount));
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        addToTop(new SetDefenseArtilleryMeterUponAction(amount));
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.secondAmount, this.amount);
    }
}

