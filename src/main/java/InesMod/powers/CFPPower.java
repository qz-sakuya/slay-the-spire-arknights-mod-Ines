package InesMod.powers;

import InesMod.action.AdHocStrategyAction;
import InesMod.action.SetPowerAction;
import InesMod.characters.Ines;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 中文名：城防炮充能
 * 敌方power
 */
public class CFPPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(CFPPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public CFPPower(AbstractCreature owner, int amount, int secondAmount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount,
                secondAmount);
    }


    @Override
    public void atStartOfTurn() { // can only monster?
        // 哪怕怪不是第一个位置，也应该在回合开始，攻击前先触发


        if (amount >= 4) {
            addToBot(new SetPowerAction(owner, owner, this,0));
        }


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
        this.description = String.format(descriptions[0], this.amount);
    }
}

