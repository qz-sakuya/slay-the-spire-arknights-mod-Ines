package InesMod.powers;

import InesMod.action.AdHocStrategyAction;
import InesMod.helpers.PathHelper;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;

/**
 * 中文名：无尽迁徙
 */
public class EternalMigrationPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(EternalMigrationPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public EternalMigrationPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer){
        if (isPlayer) {
            flash();
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, EternalMigrationPower.ID));

            // 灰蓝色特效
            addToBot(new VFXAction(new WhirlwindEffect(new Color(60/255F, 69/25F, 60/78F, 1.0F), true)));

            // 额外回合
            addToBot(new SkipEnemiesTurnAction());
        }
    }
}
