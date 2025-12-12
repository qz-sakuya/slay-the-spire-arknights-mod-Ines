package InesMod.action;

import InesMod.helpers.LoggerHelper;
import InesMod.modcore.InesModMain;
import InesMod.powers.AbstractInesPower;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
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
 * 直接设置某个能力的第二个数字
 * 目标必须具有该 power
 */
public class SetPowerSecondAmountAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    private final String powerToApplyID;
    private float startingDuration;
    private boolean flash = false;

    public SetPowerSecondAmountAction(AbstractCreature target, AbstractCreature source, String powerToApplyID, int setAmount, boolean flash) {
        if (Settings.FAST_MODE) {
            this.startingDuration = 0.1F;
        }  else {
            this.startingDuration = Settings.ACTION_DUR_FAST;
        }

        this.setValues(target, source, setAmount);
        this.duration = this.startingDuration;
        this.powerToApplyID = powerToApplyID;
        this.flash = flash;

        this.actionType = ActionType.POWER;
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.duration = 0.0F;
            this.startingDuration = 0.0F;
            this.isDone = true;
        }
    }

    public SetPowerSecondAmountAction(AbstractCreature target, AbstractCreature source, String powerToApplyID, int setAmount) {
        this(target, source, powerToApplyID, setAmount, false);
    }



    @Override
    public void update() {
        LoggerHelper.info("===SetPowerSecondAmountAction update: powerId:{},层数：{}===",powerToApplyID,amount);
        if (this.target != null && !this.target.isDeadOrEscaped()) {
            if (this.duration == this.startingDuration) {
                if (this.target instanceof AbstractMonster && this.target.isDeadOrEscaped()) {
                    this.duration = 0.0F;
                    this.isDone = true;
                    return;
                }

                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));

                for(AbstractPower p : this.target.powers) {
                    if (p.ID.equals(this.powerToApplyID) && p instanceof AbstractInesPower) {

                        ((AbstractInesPower)p).secondAmount = this.amount; // 直接赋值

                        if (this.flash) {
                            p.flash();
                        }


                        p.updateDescription();
                        // AbstractDungeon.onModifyPower();
                    }
                }
            }

            this.tickDuration();
        } else {
            this.isDone = true;
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("ApplyPowerAction");
        TEXT = uiStrings.TEXT;
    }
}
