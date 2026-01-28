package InesMod.powers.monster;

import InesMod.action.SetPowerAction;
import InesMod.action.TryAddFirePowerAction;
import InesMod.helpers.LogHelper;
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
 * 中文名：鲜血结晶
 * 敌方power
 * 图标：血色菱形
 * 等DLC第13章才会用到
 */
public class XXJJPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(XXJJPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源


    public XXJJPower(AbstractCreature owner, int amount) {
        super(ID,
                true,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
    }


    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if(damage > this.amount){
            damage = this.amount;
        }
        return damage;
    }

    @Override // 怪物是否触发需要测试
    public void onDeath() {
        LogHelper.info("===XXJJPower: onDeath===");
        // 先等待死亡动画播放完，再在怪物列表对应位置中加入新怪
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.amount);
    }
}

