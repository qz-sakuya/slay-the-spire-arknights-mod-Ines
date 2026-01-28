package InesMod.powers.player;

import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import java.util.ArrayList;

/**
 * 中文名：暗影主宰
 */
public class ShadowDominancePower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(ShadowDominancePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public ArrayList<AbstractCard> cards = new ArrayList<>();

    public ShadowDominancePower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount,
                0);

        updateShadowWhistleCnt();
    }


    @Override
    public void onCardMove(AbstractCard c, CardGroup.CardGroupType groupType) {
        if (c.cardID.equals(ShadowWhistle.ID)) {
            updateShadowWhistleCnt();
        }
    }

    // 统计手牌影哨数量
    private void updateShadowWhistleCnt(){
        int shadowWhistleCnt = 0;
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.cardID.equals(ShadowWhistle.ID)) {
                shadowWhistleCnt++;
            }
        }


        this.secondAmount = shadowWhistleCnt * amount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], amount)
                + String.format(descriptions[1], secondAmount);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            updateShadowWhistleCnt();
            return damage + this.secondAmount;
        }
        return damage;
    }

}
