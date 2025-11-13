package InesMod.powers;

import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.ModHelper;
import InesMod.modcore.InesModMain;
import InesMod.vfx.InvisibilityAuraEffect;
import InesMod.vfx.InvisibilityEffect;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;
import com.megacrit.cardcrawl.vfx.stance.WrathParticleEffect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 中文名：隐匿
 */
public class InvisibilityPower extends AbstractInesPower {
    public static final String ID = ModHelper.nameToId(InvisibilityPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源


    public InvisibilityPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
        this.isTurnBased = true;

        ArrayList<AbstractGameEffect> effect = ReflectionHacks.getPrivate(this, AbstractPower.class, "effect");
        effect.add(new InvisibilityEffect());
    }


    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount > 999) {
            this.amount = 999;
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        // 有%的字符串没法String.format
        this.description = descriptions[0] + descriptions[1] + descriptions[2] + descriptions[3];
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return blockAmount * 1.5F; // 从卡牌获得的格挡增加
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount == 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, InvisibilityPower.ID));
        } else {
            addToBot(new ReducePowerAction(this.owner, this.owner, InvisibilityPower.ID, 1));
            addToBot(new ApplyPowerAction(this.owner, this.owner, new IllusionPower(this.owner, -1)));
        }
    }


    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            // 遍历手牌
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c.cardID.equals(ShadowWhistle.ID)) {
                    // 消耗1张影哨
                    addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand));
                    break;
                }
            }
        }
    }


}
