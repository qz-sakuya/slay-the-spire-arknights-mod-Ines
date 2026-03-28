package InesMod.powers.monster;

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
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

/**
 * 中文名：鲜血补给
 * 英文名：Sanguine Infusion
 * 敌方power
 * 每回合结束时 给造物+x活力 x每回合+2
 */
public class SanguineInfusionPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(SanguineInfusionPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public SanguineInfusionPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
    }

    @Override
    public void atEndOfRound() {
        flash();
        for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
            if (mon == this.owner) {
                continue;
            }

            if (isTargetMon(mon)){
                addToBot(new ApplyPowerAction(
                        mon,
                        this.owner,
                        new EnemyVigorPower(mon, this.amount), this.amount));
            }
        }

        // this.amount += this.secondAmount;
        updateDescription();
    }


    private boolean isTargetMon(AbstractMonster mon) {
        return (mon instanceof GiftOfSanguinarch) || (mon instanceof TouchOfSanguinarch);
    }


    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], amount, secondAmount);
    }
}
