package InesMod.powers.monster;

import InesMod.cards.special.FightAgain;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
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

    public MilitaryTrainingPower(AbstractCreature owner, int amount, int toInvalidTurn) {
        super(ID,
                true,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount); // 此power不可叠加
        this.toInvalidTurn = toInvalidTurn;
    }


    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (invalidTurn > 0) {
            return damage;
        }

        float newDamage = (float) (damage * 0.4);
        int damageChange = MathUtils.floor(damage - newDamage);
        if (damageChange < 1) {
            damageChange = 1;
        }

        // 生成一张 再战
        int damageForCard = damageChange; // 向下取整
        FightAgain fightAgain = new FightAgain();
        fightAgain.baseDamage = damageForCard;
        fightAgain.damage = fightAgain.baseDamage;
        addToBot(new MakeTempCardInDrawPileAction(fightAgain,1,true,true));

        return newDamage;
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

    public void setToInvalidTurn(int amt) {
        this.toInvalidTurn = amt;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (invalidTurn == 0) {
            this.description = descriptions[0] + String.format(descriptions[1], this.toInvalidTurn);
        }
        else {
            this.description = descriptions[0] + String.format(descriptions[0] + descriptions[2], this.invalidTurn);
        }
    }
}

