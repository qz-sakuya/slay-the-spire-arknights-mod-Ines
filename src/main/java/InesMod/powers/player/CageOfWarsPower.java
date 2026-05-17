package InesMod.powers.player;

import InesMod.action.InterAction;
import InesMod.action.SpecificTriggerPowerAction;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

/**
 * 中文名：战争囚笼
 * 限制攻击目标的效果由 patch 完成
 */
public class CageOfWarsPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(CageOfWarsPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public CageOfWarsPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF, // 不视作负面
                amount);


        this.isTurnBased = true; // 白色数字
        this.renderAmountZero = true;

        LogHelper.info("===CageOfWarsPower: 所有者: {}===",this.owner.name);
        calculateMonCount();
    }





    @Override
    public void onAnyMonsterDeath(AbstractMonster mon){
        addToBot(new SpecificTriggerPowerAction(this));
    }

    @Override
    public void onSpawnMonster(AbstractMonster mon){
        addToBot(new SpecificTriggerPowerAction(this));
    }

    @Override
    public void atStartOfTurn() {
        calculateMonCount();
    }

    @Override
    public void onSpecificTrigger() {
        calculateMonCount();
    }



    public void calculateMonCount() {
        // 统计其它怪物数量
        int monCount = 0;
        for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
            LogHelper.info("===CageOfWarsPower: 统计: {}===",mon.name);
            if (mon == this.owner) {
                LogHelper.info("===CageOfWarsPower: 统计:排除自身===");
                continue;
            }

            if (mon.halfDead || mon.isDying || mon.isDead) {
                continue;
            }

            monCount += 1;
        }

        amount = monCount;

        updateDescription();
    }




    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        int actualAmount = Math.max(amount, 0);

        damage = (float) (damage * (1 + actualAmount * 0.5));
        return damage;
    }

    @Override
    public void updateDescription() {
        int actualAmount = Math.max(amount, 0);

        String tmpText = String.valueOf(actualAmount * 50) + '%';

        this.description = descriptions[0] + String.format(descriptions[1], tmpText);
    }
}
