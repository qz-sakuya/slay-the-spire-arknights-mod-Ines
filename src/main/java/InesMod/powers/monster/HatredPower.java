package InesMod.powers.monster;

import InesMod.action.SpecificTriggerPowerAction;
import InesMod.helpers.PathHelper;
import InesMod.monsters.Chapter10.GiftOfSanguinarch;
import InesMod.monsters.Chapter10.TouchOfSanguinarch;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

/**
 * 中文名：恨意
 * 英文名：Hatred
 * 敌方power
 */
public class HatredPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(HatredPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public HatredPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount,
                0);

        calculateMonCount();
    }


    @Override
    public void onAnyMonsterDeath(AbstractMonster mon){
        if (isTargetMon(mon)){
            addToBot(new SpecificTriggerPowerAction(this));
        }
    }

    @Override
    public void onSpawnMonster(AbstractMonster mon){
        if (isTargetMon(mon)){
            addToBot(new SpecificTriggerPowerAction(this));
        }
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
        // 统计新的目标怪物数量
        int monCount = 0;
        for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
            if (mon == this.owner) {
                continue;
            }

            if (mon.halfDead || mon.isDying || mon.isDead) {
                continue;
            }

            if (isTargetMon(mon)){
                monCount += 1;
            }
        }

        int newSecondAmount = monCount * amount;
        int strengthToApply = newSecondAmount - secondAmount;
        if (strengthToApply != 0) {
            flash();
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strengthToApply), strengthToApply));
        }
        secondAmount = newSecondAmount;
        updateDescription();
    }

    private boolean isTargetMon(AbstractMonster mon) {


        return (mon instanceof GiftOfSanguinarch) || (mon instanceof TouchOfSanguinarch);
    }


    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], amount)
                + String.format(descriptions[1], secondAmount);
    }
}
