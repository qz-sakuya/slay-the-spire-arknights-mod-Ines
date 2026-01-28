package InesMod.powers.player;

import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：漫漫长夜
 */
public class EndlessNightPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(EndlessNightPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public EndlessNightPower(AbstractCreature owner, int amount) {
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
        if (isPlayer) {
            boolean success = false;
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                // 保留所有影哨
                if (c.cardID.equals(ShadowWhistle.ID)) {
                    c.retain = true;
                    success = true;
                }
            }
            if (success) {
                flash();
            }
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, EndlessNightPower.ID));
        }
    }

}
