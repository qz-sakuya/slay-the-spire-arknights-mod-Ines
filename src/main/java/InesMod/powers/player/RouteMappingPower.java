package InesMod.powers.player;

import InesMod.action.ApplyPowerInSpecialRangeAction;
import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 中文名：路线绘制
 */
public class RouteMappingPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(RouteMappingPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public RouteMappingPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
    }


    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], amount);
    }

    @Override
    public void onCardMove(AbstractCard c, CardGroup.CardGroupType groupType){
        if (c instanceof ShadowWhistle
                && groupType.equals(CardGroup.CardGroupType.HAND)
                && !((ShadowWhistle)c).addedFromSameGroup) {

            flash();
            this.addToBot(new ApplyPowerInSpecialRangeAction(owner, owner, new StealsPower(owner, amount), amount,0,10));
        }
    }
}
