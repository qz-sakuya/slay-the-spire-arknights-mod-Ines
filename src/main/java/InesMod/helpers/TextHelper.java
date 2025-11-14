package InesMod.helpers;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

public class TextHelper {
    private static final UIStrings selectHandCardStrings = CardCrawlGame.languagePack.getUIString(PathHelper.nameToId("SelectHandCard"));

    // 工具方法
    // 这个方法没有实质用到，因为原版选卡框自带文本逻辑（选n张或选任意张，见"HandCardSelectScreen"文本）
    public static String selectHandCardText(int amount,
                                            boolean anyNumber,
                                            boolean canPickZero, // 在anyNumber的情况下，改为“至少”字样
                                            boolean upTo) // 与 anyNumber功能相同（两者只能开启一个），但改为“至多”字样)
    {
        String res = "";
        if (!anyNumber && !upTo) {
            res = String.format(selectHandCardStrings.TEXT[0], amount); // 选择n张牌
        }
        else if (anyNumber) {
            if (!canPickZero){
                res = selectHandCardStrings.TEXT[1]; // 选择至少1张牌
            }
            res = selectHandCardStrings.TEXT[2]; // 选择任意张牌
        }
        else { // upTo
            res = String.format(selectHandCardStrings.TEXT[3], amount); // 选择至多n张牌
        }
        return res;
    }

}
