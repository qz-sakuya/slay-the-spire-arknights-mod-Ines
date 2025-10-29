package InesMod.cards;

import InesMod.characters.Ines;
import InesMod.helpers.ModHelper;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.localization.CardStrings;


// 笔记：-2费不显示能量图标（如诅咒卡状态卡等），-1费为X费。



public abstract class AbstractInesCard extends CustomCard {
    public int consumeSteals; // 此牌消耗偷取的层数
    public AbstractInesCard(String ID,
                            boolean useTmpArt,
                            CardStrings strings,
                            int COST,
                            CardType TYPE,
                            CardRarity RARITY,
                            CardTarget TARGET,
                            CardColor color) {
        super(ID, strings.NAME, useTmpArt ? getTmpImgPath(TYPE) : getImgPath(TYPE, ID), COST, strings.DESCRIPTION, TYPE,
                color, RARITY, TARGET);

        consumeSteals = 1;
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
        return String.format("InesModResources/img/cards/%s/%s_%s.png", type, ModHelper.idToName(id), type);
    }
}