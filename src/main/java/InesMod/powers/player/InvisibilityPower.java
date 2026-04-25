package InesMod.powers.player;

import InesMod.action.ReduceAndKeepPowerAction;
import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import InesMod.vfx.InvisibilityEffect;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

/**
 * 中文名：隐匿
 */
public class InvisibilityPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(InvisibilityPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源


    public InvisibilityPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
        this.isTurnBased = true;

        this.renderAmountZero = true;
        this.endOfRoundWhenSkipMonsterTurn = true;

//        ArrayList<AbstractGameEffect> effect = ReflectionHacks.getPrivate(this, AbstractPower.class, "effect");
//        effect.add(new InvisibilityEffect());

        addEffect();
    }


    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount > 999) {
            this.amount = 999;
        }
        updateDescription();

        addEffect();
    }

    @Override
    public void updateDescription() {
        // 有%的字符串没法String.format
        this.description = descriptions[0] + descriptions[1] + descriptions[2] + descriptions[3];
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return blockAmount * 1.25F; // 从卡牌获得的格挡增加
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount == 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, InvisibilityPower.ID));
        } else {
            addToBot(new ApplyPowerAction(this.owner, this.owner, new IllusionPower(this.owner, 1)));

            // 如果有 影之疆土 ，获得力量
            AbstractPower shadowTerritoryPower = owner.getPower(ShadowTerritoryPower.ID);
            if (shadowTerritoryPower != null) {
                shadowTerritoryPower.flash();
                addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, shadowTerritoryPower.amount), shadowTerritoryPower.amount));
            }
        }

        // 延迟消除，使影哨正常享受防御增加效果
        addToBot(new ReduceAndKeepPowerAction(this.owner, this.owner, InvisibilityPower.ID, 1));
    }

    @Override
    public void atEndOfRound() {
        LogHelper.info("===InvisibilityPower：atEndOfRound===");

        // 延迟消除
        if (this.amount == 0){
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, InvisibilityPower.ID));
        }
    }


    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            ArrayList<AbstractCard> shadowWhistleGroup = new ArrayList<>();

            // 遍历手牌
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c.cardID.equals(ShadowWhistle.ID)) {
                    shadowWhistleGroup.add(c);
                }
            }

            LogHelper.info("===InvisibilityPower: shadowWhistleGroup size:{}===",shadowWhistleGroup.size());

            if (!shadowWhistleGroup.isEmpty()) {
                // 随机选取一张影哨
                int randomIndex = AbstractDungeon.cardRandomRng.random(shadowWhistleGroup.size()-1);
                LogHelper.info("===InvisibilityPower: randomIndex:{}===",randomIndex);
                AbstractCard randomCard = shadowWhistleGroup.get(randomIndex);

                // 如果有 掌握全局 能力
                AbstractPower masterTheGamePower = owner.getPower(MasterTheGamePower.ID);
                if (masterTheGamePower != null) {
                    // 丢弃1张影哨
                    addToTop(new DiscardSpecificCardAction(randomCard, AbstractDungeon.player.hand));
                }
                else{
                    // 否则消耗1张影哨
                    addToTop(new ExhaustSpecificCardAction(randomCard, AbstractDungeon.player.hand));
                }
            }
        }


    }



    private void addEffect(){
        if (!(this.owner instanceof AbstractPlayer)) {
            return;
        }

        ArrayList<AbstractGameEffect> effect = ReflectionHacks.getPrivate(this, AbstractPower.class, "effect");

        boolean hasSameType = false;
        for (AbstractGameEffect existingEffect : effect) {
            if (existingEffect instanceof InvisibilityEffect) {
                hasSameType = true;
                break;
            }
        }

        if (!hasSameType) {
            effect.add(new InvisibilityEffect());
        }
    }
}
