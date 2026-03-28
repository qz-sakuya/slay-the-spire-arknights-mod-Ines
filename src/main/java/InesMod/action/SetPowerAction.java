package InesMod.action;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
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
 * 直接设置某个能力的值
 * 只在ApplyPowerAction的基础上，将叠加改为直接赋值
 */
public class SetPowerAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    private AbstractPower powerToApply;
    private float startingDuration;

    public SetPowerAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int setAmount) {
        if (Settings.FAST_MODE) {
            this.startingDuration = 0.1F;
        }  else {
            this.startingDuration = Settings.ACTION_DUR_FAST;
        }

        this.setValues(target, source, setAmount);
        this.duration = this.startingDuration;
        this.powerToApply = powerToApply;

        this.actionType = ActionType.POWER;
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.duration = 0.0F;
            this.startingDuration = 0.0F;
            this.isDone = true;
        }
    }



    @Override
    public void update() {
        if (this.target != null && !this.target.isDeadOrEscaped()) {
            if (this.duration == this.startingDuration) {
                if (this.powerToApply instanceof NoDrawPower && this.target.hasPower(this.powerToApply.ID)) {
                    this.isDone = true;
                    return;
                }

                if (this.source != null) {
                    for(AbstractPower pow : this.source.powers) {
                        pow.onApplyPower(this.powerToApply, this.target, this.source);
                    }
                }

                if (AbstractDungeon.player.hasRelic("Champion Belt") && this.source != null && this.source.isPlayer && this.target != this.source && this.powerToApply.ID.equals("Vulnerable") && !this.target.hasPower("Artifact")) {
                    AbstractDungeon.player.getRelic("Champion Belt").onTrigger(this.target);
                }

                if (this.target instanceof AbstractMonster && this.target.isDeadOrEscaped()) {
                    this.duration = 0.0F;
                    this.isDone = true;
                    return;
                }

                if (AbstractDungeon.player.hasRelic("Ginger") && this.target.isPlayer && this.powerToApply.ID.equals("Weakened")) {
                    AbstractDungeon.player.getRelic("Ginger").flash();
                    this.addToTop(new TextAboveCreatureAction(this.target, TEXT[1]));
                    this.duration -= Gdx.graphics.getDeltaTime();
                    return;
                }

                if (AbstractDungeon.player.hasRelic("Turnip") && this.target.isPlayer && this.powerToApply.ID.equals("Frail")) {
                    AbstractDungeon.player.getRelic("Turnip").flash();
                    this.addToTop(new TextAboveCreatureAction(this.target, TEXT[1]));
                    this.duration -= Gdx.graphics.getDeltaTime();
                    return;
                }

                if (this.target.hasPower("Artifact") && this.powerToApply.type == AbstractPower.PowerType.DEBUFF) {
                    this.addToTop(new TextAboveCreatureAction(this.target, TEXT[0]));
                    this.duration -= Gdx.graphics.getDeltaTime();
                    CardCrawlGame.sound.play("NULLIFY_SFX");
                    this.target.getPower("Artifact").flashWithoutSound();
                    this.target.getPower("Artifact").onSpecificTrigger();
                    return;
                }

                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
                boolean hasBuffAlready = false;

                for(AbstractPower p : this.target.powers) {
                    if (p.ID.equals(this.powerToApply.ID) && !p.ID.equals("Night Terror")) {

                        // 直接赋值，但保留对于stackPower的调用（便于触发重载的stackPower）
                        p.amount = this.amount;
                        p.stackPower(0);

                        p.flash();
                        if ((p instanceof StrengthPower || p instanceof DexterityPower) && this.amount <= 0) {
                            AbstractDungeon.effectList.add(new PowerDebuffEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, this.powerToApply.name + TEXT[3]));
                        } else if (this.amount > 0) {
                            if (p.type != AbstractPower.PowerType.BUFF && !(p instanceof StrengthPower) && !(p instanceof DexterityPower)) {
                                AbstractDungeon.effectList.add(new PowerDebuffEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, "+" + Integer.toString(this.amount) + " " + this.powerToApply.name));
                            } else {
                                AbstractDungeon.effectList.add(new PowerBuffEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, "+" + Integer.toString(this.amount) + " " + this.powerToApply.name));
                            }
                        } else if (p.type == AbstractPower.PowerType.BUFF) {
                            AbstractDungeon.effectList.add(new PowerBuffEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, this.powerToApply.name + TEXT[3]));
                        } else {
                            AbstractDungeon.effectList.add(new PowerDebuffEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, this.powerToApply.name + TEXT[3]));
                        }

                        p.updateDescription();
                        hasBuffAlready = true;
                        AbstractDungeon.onModifyPower();
                    }
                }

                if (this.powerToApply.type == AbstractPower.PowerType.DEBUFF) {
                    this.target.useFastShakeAnimation(0.5F);
                }

                if (!hasBuffAlready) {
                    this.target.powers.add(this.powerToApply); // 添加新能力到列表
                    Collections.sort(this.target.powers);
                    this.powerToApply.onInitialApplication();
                    this.powerToApply.flash();
                    if (this.amount >= 0 || !this.powerToApply.ID.equals("Strength") && !this.powerToApply.ID.equals("Dexterity") && !this.powerToApply.ID.equals("Focus")) {
                        if (this.powerToApply.type == AbstractPower.PowerType.BUFF) {
                            AbstractDungeon.effectList.add(new PowerBuffEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, this.powerToApply.name));
                        } else {
                            AbstractDungeon.effectList.add(new PowerDebuffEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, this.powerToApply.name));
                        }
                    } else {
                        AbstractDungeon.effectList.add(new PowerDebuffEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, this.powerToApply.name + TEXT[3]));
                    }

                    AbstractDungeon.onModifyPower();
                    if (this.target.isPlayer) {
                        int buffCount = 0;

                        for(AbstractPower p : this.target.powers) {
                            if (p.type == AbstractPower.PowerType.BUFF) {
                                ++buffCount;
                            }
                        }

                        if (buffCount >= 10) {
                            UnlockTracker.unlockAchievement("POWERFUL");
                        }
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
