package InesMod.powers.player;

import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：影散
 * 此 power 的效果由 ShadowWhistle 代行
 */
public class ShadowDispersePower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(ShadowDispersePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public ShadowDispersePower(AbstractCreature owner, int amount) {
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

//    @Override
//    public void onCardMove(AbstractCard c, CardGroup.CardGroupType groupType) {
//        if (c instanceof ShadowWhistle && groupType == CardGroup.CardGroupType.EXHAUST_PILE){
//
//            flash();
//            addToBot(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//        }
//    }

    @Override
    public void onExhaust(AbstractCard card){
        if (card.cardID.equals(ShadowWhistle.ID)) {
            addToBot(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }
}
