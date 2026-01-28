package InesMod.powers.monster;

import InesMod.action.SetPowerAction;
import InesMod.action.TryAddFirePowerAction;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：重生造物
 * 敌方power
 * 图标：血滴
 */
public class CSZWPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(CSZWPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源



    public CSZWPower(AbstractCreature owner, int amount) {
        super(ID,
                true,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount); // 不可叠加
    }


    // 小造物HP20/24 大造物40/45,进阶加成：普通加成7/6,精英9/8
        // 本体=大约2倍

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        damage = (float) (damage * 0.5);
        return damage;
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0]);
    }
}

