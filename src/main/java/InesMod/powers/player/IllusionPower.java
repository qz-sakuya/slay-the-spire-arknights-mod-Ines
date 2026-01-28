package InesMod.powers.player;

import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：虚影
 * 一个很有趣的事情是，虚影因为回合最后添加，一般在“缓冲”后面，所以优先消耗缓冲，这也是强度的一个设定
 */
public class IllusionPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(IllusionPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public IllusionPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
    }



    @Override
    public void updateDescription() {
        StringBuilder energyText = new StringBuilder();
        for (int i = 0; i<this.amount; i++) {
            energyText.append(" [E] ");
        }
        this.description = String.format(descriptions[0], this.amount, energyText);
    }


    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new ReducePowerAction(this.owner, this.owner, IllusionPower.ID,this.amount));
        addToBot(new GainEnergyAction(this.amount));
    }




    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            addToTop(new ReducePowerAction(this.owner, this.owner, IllusionPower.ID,1));
        }
        return 0; // 使伤害归零
    }
}
