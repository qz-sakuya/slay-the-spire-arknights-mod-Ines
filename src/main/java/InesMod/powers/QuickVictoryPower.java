package InesMod.powers;

import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 中文名：速战速决
 * 此power的部分效果由InvisibilityPower代行
 */
public class QuickVictoryPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(QuickVictoryPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public QuickVictoryPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount); // 此power不可叠加
    }


    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.amount);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        // 如果有隐匿，造成双倍伤害
        AbstractPower invisibilityPower = owner.getPower(InvisibilityPower.ID);
        if (invisibilityPower != null && type == DamageInfo.DamageType.NORMAL) {
            return damage * 2.0F;
        }
        return damage;
    }
}
