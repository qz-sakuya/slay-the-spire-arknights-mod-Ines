package InesMod.powers.player;

import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：掌控全局
 * 此 power 的效果由 InvisibilityPower 代行
 */
public class MasterTheGamePower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(MasterTheGamePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public MasterTheGamePower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount); // 此power不可叠加
    }


    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.amount);
    }


    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer)
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, MasterTheGamePower.ID));
    }


}
