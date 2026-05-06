package InesMod.cards.skill;

import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.patchs.ExhaustCountInCombatManager;
import InesMod.powers.player.InterPower;
import InesMod.powers.player.InvisibilityPower;
import InesMod.powers.player.StealsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：经验积累
 */
public class ExperienceGain extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(ExperienceGain.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public int exhaustNeed;
    public int costHasDecreased = 0;  // 因为牌效而减费的量

    public ExperienceGain() {
        super(ID,
                false,
                cardStrings,
                4,
                CardType.SKILL,
                CardRarity.RARE,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
        this.exhaustNeed = 3;

        this.cardsToPreview = new ShadowWhistle();

        // 在这里储存消耗数
        this.baseMagicNumber = ExhaustCountInCombatManager.exhaustCountInCombat;
    }
 
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new InterPower(p, 1), 1));
        this.addToBot(new ApplyPowerAction(p, p, new InvisibilityPower(p, 1), 1));
        this.addToBot(new ApplyPowerAction(p, p, new StealsPower(p, 1), 1));
        this.addToBot(new MakeTempCardInHandAction(new ShadowWhistle(), 1)); // 生成1张影哨
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        this.baseMagicNumber = ExhaustCountInCombatManager.exhaustCountInCombat;
        tryDecreaseCost();
    }


    @Override
    public void onExhaust(AbstractCard c) {
        this.baseMagicNumber += 1;
        tryDecreaseCost();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.exhaustNeed -= 1;
            tryDecreaseCost();

            // 火堆等界面不显示额外信息
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();

            // 如果在战斗中升级，后续 applyPowers() 会重新渲染文本的
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        ExperienceGain card = (ExperienceGain) super.makeStatEquivalentCopy();

        // 深拷贝该值，以避免升级界面显示错误的升级后费用
        card.costHasDecreased = this.costHasDecreased;
        return card;
    }

    @Override
    public void atBattleStartPreDraw() {
        // 重置自定义变量
        this.costHasDecreased = 0;
        this.baseMagicNumber = 0;
    }

    // 辅助方法
    public void tryDecreaseCost() {
        int costCanDecrease = baseMagicNumber / exhaustNeed; // 向下取整
        while(costCanDecrease > costHasDecreased) { // 之前减过就不会再减
            this.updateCost(-1);
            costHasDecreased++;
        }

        // 重新渲染文本，添加额外信息
        if (!upgraded) {
            this.rawDescription = cardStrings.DESCRIPTION;
        }
        else {
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        }
        this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }
}
