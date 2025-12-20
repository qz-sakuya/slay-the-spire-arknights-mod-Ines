package InesMod.action;

import InesMod.cards.attack.OnTheBrink;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
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
 * 重置一触即发
 */
public class ResetOnTheBrinkAction extends AbstractGameAction {
    AbstractCard card;

    public ResetOnTheBrinkAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        if (card instanceof OnTheBrink) {
            this.card.damage = this.card.baseDamage = ((OnTheBrink) card).initDamage;
            this.card.isDamageModified = false;

            this.card.baseMagicNumber = 0;

            this.card.applyPowers();
        }

        this.isDone = true;
    }

}
