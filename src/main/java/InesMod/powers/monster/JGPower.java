package InesMod.powers.monster;

import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：坚固
 * 受到的所有伤害至多为5。
 * 敌方power
 * 等DLC第13章才会用到
 */
public class JGPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(JGPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源


    public JGPower(AbstractCreature owner, int amount) {
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


    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.amount);
    }
}

