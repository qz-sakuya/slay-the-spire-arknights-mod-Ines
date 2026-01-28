package InesMod.powers.monster;

import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

/**
 * 中文名：枯木新枝
 * 敌方power
 * 图标
 */
public class KMXZPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(KMXZPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public KMXZPower(AbstractCreature owner, int amount, boolean spawnElite) {
        super(ID,
                true,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
    }

    @Override // 成功造成伤害时
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
            AbstractPower powerToGet = mon.getPower(CFPPower.ID);
            if (powerToGet instanceof CFPPower && powerToGet.amount < ((CFPPower) powerToGet).secondAmount) {
                addToBot(new ApplyPowerAction(mon, owner, new CFPPower(mon, 1,((CFPPower) powerToGet).secondAmount,((CFPPower) powerToGet).damage), 1));
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0]);
    }
}

