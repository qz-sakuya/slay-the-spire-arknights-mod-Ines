package InesMod.cards;

import InesMod.helpers.PathHelper;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.localization.CardStrings;




/**
 * 作为UI的牌
 */
public abstract class AbstractInesUICard extends CustomCard {
    public boolean available = true;

    public AbstractInesUICard(String ID,
                              boolean useTmpArt,
                              CardStrings strings,
                              CardType TYPE,
                              CardRarity RARITY) {
        super(ID, strings.NAME, useTmpArt ? getTmpImgPath(TYPE) : getImgPath(TYPE, ID), -2, strings.DESCRIPTION, TYPE,
                CardColor.COLORLESS, RARITY, CardTarget.NONE);
    }

    public AbstractInesUICard(String ID,
                              boolean useTmpArt,
                              CardStrings strings) {
        super(ID, strings.NAME, useTmpArt ? getTmpImgPath(CardType.SKILL) : getImgPath(CardType.SKILL, ID), -2, strings.DESCRIPTION, CardType.SKILL,
                CardColor.COLORLESS, CardRarity.COMMON, CardTarget.NONE);
    }

    public void JudgeAvailability(){}


    private static String getImgPath(CardType t, String id) {
        String type;
        switch (t) {
            case ATTACK:
                type = "attack";
                break;
            case POWER:
                type = "power";
                break;
            case STATUS:
                type = "status";
                break;
            case CURSE:
                type = "curse";
                break;
            case SKILL:
                type = "skill";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + t);
        }
        return String.format("InesModResources/img/cards/ui/%s_%s.png", PathHelper.idToName(id), type);
    }

    private static String getTmpImgPath(CardType t) {
        String type;
        switch (t) {
            case ATTACK:
                type = "attack";
                break;
            case POWER:
                type = "power";
                break;
            case STATUS:
            case CURSE:
            case SKILL:
                type = "skill";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + t);
        }
        return String.format("InesModResources/img/cards/test/test_%s.png", type);
    }
}