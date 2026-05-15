package InesMod.powers.monster;

import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 中文名：枯木新枝
 * 英文名：New Branches
 * 敌方power
 * 图标
 */
public class NewBranchesPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(NewBranchesPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public NewBranchesPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount // 不可叠加
        );
    }

    @Override // 成功造成伤害时
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
            AbstractPower powerToGet = mon.getPower(DefenseArtilleryMeterPower.ID);
            if (powerToGet instanceof DefenseArtilleryMeterPower && powerToGet.amount < ((DefenseArtilleryMeterPower) powerToGet).secondAmount) {
                addToTop(new ApplyPowerAction(mon, owner, new DefenseArtilleryMeterPower(mon, 1,((DefenseArtilleryMeterPower) powerToGet).secondAmount,((DefenseArtilleryMeterPower) powerToGet).damage), 1));
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0]);
    }
}

