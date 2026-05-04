package InesMod.powers.player;

import InesMod.action.DelayToAddAction;
import InesMod.action.SpecificTriggerPowerAction;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 中文名：佣兵手段
 * 此 power 的效果由 StealsPower 、 InsightPower 和 ApplyStealsToTargetAction 代行
 */
public class MercenaryTacticsPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(MercenaryTacticsPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public MercenaryTacticsPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount); // 不可叠加

        // 如果有 偷取 ，触发其描述更新
        AbstractPower powerToGet = owner.getPower(StealsPower.ID);
        if (powerToGet != null) {
            addToBot(new DelayToAddAction(new SpecificTriggerPowerAction(powerToGet)));
        }
    }


    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], 1);
    }






}
