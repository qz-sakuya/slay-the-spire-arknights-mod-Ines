package InesMod.cards.status;

import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 中文卡名：影哨
 */
public class ShadowWhistle extends AbstractInesCard {
    public static final String ID = ModHelper.nameToId(ShadowWhistle.class.getSimpleName());
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

    public void onMoveToDiscard() {
        this.addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.block));
        addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.discardPile));
    }

    @Override
    public void upgrade() {}
}
