package InesMod.powers.player;

import InesMod.action.InformantAction;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import java.util.ArrayList;

/**
 * 中文名：线人
 */
public class InformantPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(InformantPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public ArrayList<AbstractCard> cards = new ArrayList<>();

    public InformantPower(AbstractCreature owner, int amount, ArrayList<AbstractCard> inputCards) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount); // 此power不可叠加

        this.cards = new ArrayList<>();
        if (inputCards != null) {
            for (AbstractCard c : inputCards) {
                if (c != null) {
                    this.cards.add(c);
                }
            }
        }

        updateDescription();
    }

    public void addCards(ArrayList<AbstractCard> moreCards) {
        flash();
        if (moreCards != null) {
            for (AbstractCard c : moreCards) {
                if (c != null) {
                    this.cards.add(c);
                }
            }
        }

        updateDescription();
    }


    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder();
        if (cards != null) {
            for (AbstractCard c : cards) {
                sb.append(" NL  #b").append(c.name).append(" ");
            }
        }

        this.description = String.format(descriptions[0], sb);
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new InformantAction((AbstractPlayer) owner, cards));
    }

}
