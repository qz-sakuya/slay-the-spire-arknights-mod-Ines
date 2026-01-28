package InesMod.powers.player;

import InesMod.cards.skill.PhotographicMemory;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：过目不忘
 */
public class PhotographicMemoryPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(PhotographicMemoryPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源


    public PhotographicMemoryPower(AbstractCreature owner, int amount, int secondAmount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount,
                secondAmount);
    }


    @Override
    public void updateDescription() {
        if (this.amount <= 1){
            this.description = String.format(descriptions[0], this.amount, this.secondAmount);
        }
        else {
            this.description = String.format(descriptions[1], this.amount, this.secondAmount);
        }
    }



    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (!card.cardID.equals(PhotographicMemory.ID)) { // 不能复制同名牌
            AbstractCard newCard = card.makeStatEquivalentCopy();

            if (newCard.cost >= 0 && newCard.cost != this.secondAmount) {
                newCard.cost = this.secondAmount;
                newCard.isCostModified = true;
            }
            newCard.costForTurn = newCard.cost;


            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            this.addToBot(new MakeTempCardInDrawPileAction(newCard, 1, true, true));
        }
    }


}
