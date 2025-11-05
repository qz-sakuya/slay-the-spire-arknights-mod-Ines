package InesMod.powers;

import InesMod.action.InterAction;
import InesMod.action.SetPowerAction;
import InesMod.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：情报
 */
public class InterPower extends AbstractInesPower {
    public static final String ID = ModHelper.nameToId(InterPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public InterPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
    }

    @Override
    public void onCardDraw(AbstractCard card){
        addToBot(new InterAction((AbstractPlayer) owner, 1));
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.amount);
    }
}
