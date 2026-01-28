package InesMod.powers.monster;

import InesMod.cards.special.FightAgain;
import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static java.lang.Math.floor;

/**
 * 中文名：军事传统
 * 敌方power
 * 图标
 *
 * 受到攻击时闪避，减免60%伤害，将
 * 降低最大amount点伤害
 *
 * 暂时失效的效果由 炮击！power 代行
 */
public class JSCTPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(JSCTPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    int invalidTurn = 0;

    public JSCTPower(AbstractCreature owner, int amount) {
        super(ID,
                true,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
    }


    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        float newDamage = (float) (damage * 0.4);
        int damageChange = MathUtils.floor(damage - newDamage);
        if (damageChange < 1) {
            damageChange = 1;
        }

        // 生成一张 再战
        FightAgain fightAgain = new FightAgain();
        fightAgain.baseDamage = damageChange;
        fightAgain.damage = fightAgain.baseDamage;
        addToBot(new MakeTempCardInDrawPileAction(fightAgain,1,true,true));

        return newDamage;
    }

    @Override
    public void atEndOfRound() {
        invalidTurn -= 1;
        if (invalidTurn <= 0) {
            invalidTurn = 0;
        }
        updateDescription();
    }


    @Override
    public void updateDescription() {
        if (invalidTurn == 0) {
            this.description = String.format(descriptions[0] + descriptions[1]);
        }
        else {
            this.description = String.format(descriptions[0] + descriptions[2], this.invalidTurn);
        }

    }
}

