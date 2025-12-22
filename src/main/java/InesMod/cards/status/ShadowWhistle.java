package InesMod.cards.status;

import InesMod.action.SimpleExhaustAction;
import InesMod.cards.AbstractInesCard;
import InesMod.helpers.PathHelper;
import InesMod.powers.InformantPower;
import InesMod.powers.ShadowWhistleRetrievalPower;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 中文卡名：影哨
 */
public class ShadowWhistle extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(ShadowWhistle.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public ShadowWhistle() {
        super(ID,
                false,
                cardStrings,
                -2,
                CardType.STATUS,
                CardRarity.COMMON,
                CardTarget.NONE,
                CardColor.COLORLESS);
        this.block = this.baseBlock = 6;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    // 不建议使用OnMoveToDiscard
    public void onCardMove(AbstractCard c, CardGroup.CardGroupType groupType) {
        if (c == this && groupType == CardGroup.CardGroupType.DISCARD_PILE){
            addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.block));

            // 如果有 影哨回收 额外获得格挡
            AbstractPower shadowWhistleRetrievalPower = AbstractDungeon.player.getPower(ShadowWhistleRetrievalPower.ID);
            if (shadowWhistleRetrievalPower != null) {
                shadowWhistleRetrievalPower.flash();
                addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, shadowWhistleRetrievalPower.amount));
            }

            addToBot(new SimpleExhaustAction(this, AbstractDungeon.player.discardPile));
        }
    }

    @Override
    public void upgrade() {}
}
