package InesMod.powers;

import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：武器储备
 */
public class WeaponStockpilePower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(WeaponStockpilePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public WeaponStockpilePower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
        this.isTurnBased = true;
    }


    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.amount);
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount == 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, WeaponStockpilePower.ID));
        } else {
            addToBot(new ReducePowerAction(this.owner, this.owner, WeaponStockpilePower.ID, 1));
            this.addToBot(new MakeTempCardInHandAction(new Shiv(), 2)); // 生成2张小刀
        }
    }


}
