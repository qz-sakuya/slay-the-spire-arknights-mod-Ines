package InesMod.powers.monster;

import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：重生造物
 * 英文名：Reborn Creation
 * 敌方power
 * 图标：血滴
 */
public class RebornCreationPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(RebornCreationPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源



    public RebornCreationPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
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
        this.description = descriptions[0];
    }
}

