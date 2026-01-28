package InesMod.powers.monster;

import InesMod.action.SetPowerAction;
import InesMod.action.TryAddFirePowerAction;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：城防炮充能
 * 敌方power
 * 图标：透视准心+斜向下箭头
 */
public class CFPPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(CFPPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    final int damage;

    public CFPPower(AbstractCreature owner, int amount, int secondAmount, int damage) {
        super(ID,
                true,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount,
                secondAmount);
        this.damage = damage;

        this.priority = 0; // 最左侧
    }


    @Override
    public void atStartOfTurn() { // can only monster?
        // 哪怕怪不是第一个位置，也应该在回合开始，攻击前先触发

        // 回合开始时才归零
        if (amount >= 4) {
            addToBot(new SetPowerAction(owner, owner, this,0));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) { // can only monster?
        addToBot(new ApplyPowerAction(owner, owner, this,1));
        addToBot(new TryAddFirePowerAction(owner, owner, damage));
    }



    @Override // 重载，使得层数为0时也可以绘制数字
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        if (this.amount >= 0) {
            // 默认白色
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
        }
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.secondAmount, this.amount);
    }
}

