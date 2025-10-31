package InesMod.action;

import InesMod.modcore.InesModMain;
import InesMod.powers.AgentVanguardPower;
import InesMod.powers.InterPower;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerDebuffEffect;

import java.util.Collections;

/**
 * 情报 的效果
 * 判断手牌是否未满，然后抽1张牌
 */
public class InterAction extends AbstractGameAction {
    AbstractCreature source;
    int amount;

    public InterAction(AbstractPlayer source, int drawAmount) {
        this.source = source;
        this.amount = drawAmount;
    }



    @Override
    public void update() {
        InesModMain.logger.info("===情报action：开始===");
        AbstractPlayer p = (AbstractPlayer)source;

        if (!p.hasPower("No Draw") && p.hand.size() < 10) {
            InesModMain.logger.info("===情报action：抽牌上限未满===");
            int cardToDraw = 1;

            // 如果有情报
            AbstractPower powerToFind = p.getPower(InterPower.ID);
            if (powerToFind != null) {
                InesModMain.logger.info("===情报action：具有情报===");
                powerToFind.flash();
                addToTop(new DrawCardAction(source, cardToDraw));
                addToTop(new ReducePowerAction(this.source, this.source, InterPower.ID, cardToDraw));
            }
        }

        this.isDone = true;
    }

}
