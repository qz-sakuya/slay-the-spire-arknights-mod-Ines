package InesMod.powers.monster;

import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：弹射
 * 英文名：Bounce
 * 敌方power
 * 如果目标具有格挡，造成双倍伤害
 */
public class BouncePower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(BouncePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public BouncePower(AbstractCreature owner, int amount, boolean spawnElite) {
        super(ID,
                true,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount); // 不可叠加
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        float newDamage = damage;
        if (AbstractDungeon.player.currentBlock > 0) {
            newDamage *= 2;
        }
        return newDamage;
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0]);
    }
}

