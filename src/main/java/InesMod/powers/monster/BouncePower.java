package InesMod.powers.monster;

import InesMod.helpers.LogHelper;
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

    public BouncePower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount); // 不可叠加
    }

    @Override
    public int OnAttackBeforeBlock(DamageInfo info, int damageAmount, int currentBlock) {
        LogHelper.info("===BouncePower: OnAttackBeforeBlock: damageAmount = {},玩家格挡={}===",currentBlock);
        int newDamage = damageAmount;
        if (currentBlock > 0) {
            newDamage *= 2;
        }
        return newDamage;
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0]);
    }
}

