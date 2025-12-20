package InesMod.cards.skill;

import InesMod.action.OneByOneAction;
import InesMod.action.SelectHandCardAction;
import InesMod.action.SelectPileCardAction;
import InesMod.cards.AbstractInesCard;
import InesMod.cards.status.ShadowWhistle;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

/**
 * 中文卡名：斗争往事
 */
public class BattleMemories extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(BattleMemories.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    public BattleMemories() {
        super(ID,
                false,
                cardStrings,
                3,
                CardType.SKILL,
                CardRarity.RARE,
                CardTarget.SELF,
                Ines.Enums.INES_CARD);
        this.magicNumber = this.baseMagicNumber = 2;

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 先分别排序抽牌堆和弃牌堆
        CardGroup sortedDrawPile = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            sortedDrawPile.addToTop(card);
        }
        sortedDrawPile.sortAlphabetically(true);
        sortedDrawPile.sortByRarityPlusStatusCardType(false);

        CardGroup sortedDiscardPile = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            sortedDiscardPile.addToTop(card);
        }
        sortedDiscardPile.sortAlphabetically(true);
        sortedDiscardPile.sortByRarityPlusStatusCardType(false);

        // 然后合并
        ArrayList<AbstractCard> cardList = new ArrayList<>();
        cardList.addAll(sortedDrawPile.group);
        cardList.addAll(sortedDiscardPile.group);

        addToBot(new SelectPileCardAction(
                cardList,
                "加入手牌",
                magicNumber,
                c -> c.type == CardType.POWER,
                (selected) -> {
                    for (AbstractCard c : selected) {
                        if (c != null) {
                            c.setCostForTurn(-99);
                            if (AbstractDungeon.player.drawPile.contains(c)) {
                                AbstractDungeon.player.drawPile.moveToHand(c);
                            }
                            else if (AbstractDungeon.player.discardPile.contains(c)) {
                                AbstractDungeon.player.discardPile.moveToHand(c);
                            }
                        }
                    }
                    AbstractDungeon.player.hand.refreshHandLayout();
                },
                true,
                false
        ));
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }
}
