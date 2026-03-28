package InesMod.powers.monster;

import InesMod.action.DelayToAddAction;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;



/**
 * 中文名：活力
 * 活力的敌方版本
 */
public class EnemyVigorPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(EnemyVigorPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    // public boolean toRemove = false;

    public EnemyVigorPower(AbstractCreature owner, int amount) {
        super(ID,
                true,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);


        // 使用原版图标
        this.loadRegion("vigor");
    }


    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage + (float)this.amount : damage;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        LogHelper.info("===EnemyVigorPower: onAttack， owner：{}===",this.owner.name);

        // 延迟，以服务多段攻击
        this.addToBot(new DelayToAddAction(new RemoveSpecificPowerAction(this.owner, this.owner, EnemyVigorPower.ID)));
    }



    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], amount);
    }
}


