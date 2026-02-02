package InesMod.powers.monster;

import InesMod.helpers.PathHelper;
import InesMod.monsters.SKZ_ZS;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

/**
 * 中文名：血脉
 * 敌方power
 */
public class XMPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(XMPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public XMPower(AbstractCreature owner, int amount) {
        super(ID,
                true,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount,
                0);
    }


    @Override
    public void onDeath(){
        Work();
    }

    @Override
    public void onSpawnMonster(AbstractMonster mon){
        if (isTargetMon(mon)){
            Work();
        }
    }

    @Override
    public void atStartOfTurn() {
        Work();
    }


    private void Work() {
        int monCount = 0;
        for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
            if (mon == this.owner) {
                continue;
            }

            if (isTargetMon(mon)){
                monCount += 1;
            }
        }

        int newSecondAmount = monCount * amount;
        int strengthToApply = newSecondAmount - secondAmount;
        addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strengthToApply), strengthToApply));
        secondAmount = newSecondAmount;
        updateDescription();
    }

    private boolean isTargetMon(AbstractMonster mon) {
        return (mon instanceof SKZ_ZS); // TODO：改为2个正确ID
    }


    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], amount)
                + String.format(descriptions[1], secondAmount);
    }
}
