package InesMod.powers;

import InesMod.cards.status.ShadowWhistle;
import InesMod.helpers.PathHelper;
import InesMod.modcore.InesModMain;
import InesMod.vfx.InvisibilityEffect;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
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
        return blockAmount * 1.25F; // 从卡牌获得的格挡增加
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount == 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, InvisibilityPower.ID));
        } else {
            int numToChange = 1;

            // 如果有 速战速决 ，额外转化1层隐匿
            AbstractPower quickVictoryPower = owner.getPower(QuickVictoryPower.ID);
            if (quickVictoryPower != null && this.amount >= 2) {
                numToChange = 2;
            }

            addToBot(new ReducePowerAction(this.owner, this.owner, InvisibilityPower.ID, numToChange));
            addToBot(new ApplyPowerAction(this.owner, this.owner, new IllusionPower(this.owner, numToChange)));
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

            InesModMain.logger.info("===InvisibilityPower: shadowWhistleGroup size:{}===",shadowWhistleGroup.size());

            if (!shadowWhistleGroup.isEmpty()) {
                // 随机选取一张影哨
                int randomIndex = AbstractDungeon.cardRandomRng.random(shadowWhistleGroup.size()-1);
                InesModMain.logger.info("===InvisibilityPower: randomIndex:{}===",randomIndex);
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


}
