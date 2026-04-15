package InesMod.powers.monster;

import InesMod.cards.special.Counter;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：军事训练
 * 英文名：Military Training
 * 敌方power
 * 暂时失效的效果由 炮击！power 代行
 */
public class MilitaryTrainingPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(MilitaryTrainingPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    boolean isThisTurnInvalid = false;
    int invalidTurn = 0;
    int toInvalidTurn;

    int damageForCard = 1;
    boolean createCard = false;

    public MilitaryTrainingPower(AbstractCreature owner, int amount, int toInvalidTurn) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount); // 此power不可叠加
        this.toInvalidTurn = toInvalidTurn;

        this.priority = 9000; // 排在易伤等的右侧

        updateDescription();
    }


    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type)  {
        if (invalidTurn > 0) {
            return damage;
        }

        if (damage > 0 && type == DamageInfo.DamageType.NORMAL) {
            // 减免伤害
            float newDamage = (float) (damage * 0.4);

            int damageChange = Math.round(damage - newDamage); // 四舍五入
            if (damageChange < 1) {
                damageChange = 1;
            }
            this.damageForCard = damageChange;
            this.createCard = true;

            return newDamage;
        }

        return damage;
    }


    // 仅生成 回击
    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damage)  {
        if (invalidTurn > 0) {
            return damage;
        }

        if (createCard) {
            // 生成一张 回击
            Counter newCard = new Counter();
            newCard.baseDamage =  this.damageForCard;
            newCard.damage = newCard.baseDamage;
            addToBot(new MakeTempCardInDrawPileAction(newCard,1,true,true));

            createCard = false;
        }

        return damage;
    }




    @Override
    public void atEndOfRound() {
        if (isThisTurnInvalid) {
            isThisTurnInvalid = false; // 刚被失效的回合（受到城防炮的回合）结束，不会使invalidTurn-1
        }
        else{
            invalidTurn -= 1;
        }

        if (invalidTurn < 0) {
            invalidTurn = 0;
        }
        updateDescription();
    }


    public void invalid() {
        this.isThisTurnInvalid = true;
        this.invalidTurn += this.toInvalidTurn;
        updateDescription();
    }

    public void clearInvalidTurn() {
        invalidTurn = 0;
    }

    public void setToInvalidTurn(int amt) {
        this.toInvalidTurn = amt;
        updateDescription();
    }

    @Override // 重写，使得绘制失效回合数（红色）
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        if (this.invalidTurn > 0) {
            this.redColor.a = c.a;
            c = this.redColor;
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.invalidTurn), x, y, this.fontScale, c);
        }
    }



    @Override
    public void updateDescription() {
        if (invalidTurn == 0) {
            this.description = descriptions[0] + String.format(descriptions[1], this.toInvalidTurn);
        }
        else {
            this.description = descriptions[0] + String.format(descriptions[2], this.invalidTurn);
        }
    }
}

