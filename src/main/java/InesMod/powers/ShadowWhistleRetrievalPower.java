package InesMod.powers;

import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：影哨回收
 */
public class ShadowWhistleRetrievalPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(ShadowWhistleRetrievalPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public ShadowWhistleRetrievalPower(AbstractCreature owner, int amount) {
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
    public void onExhaust(AbstractCard card){
        if (card.cardID.equals(ShadowWhistle.ID)) {
            flash();
            this.addToBot(new GainBlockAction(this.owner, this.amount, Settings.FAST_MODE));
        }
    }
}
