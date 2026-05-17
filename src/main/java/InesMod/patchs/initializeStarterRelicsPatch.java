package InesMod.patchs;

import InesMod.characters.Ines;
import InesMod.enums.InesCardTags;
import InesMod.helpers.ConfigHelper;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.relics.ShadowOfLondinium;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

/**
 *  为其它角色添加额外层级遗物
 */
public class initializeStarterRelicsPatch {


    @SpirePatch(clz = AbstractPlayer.class, method = "initializeStarterRelics")
    public static class Fun {
        @SpireInsertPatch(rloc = 1, localvars = {"relics"})
        public static void Insert(AbstractPlayer __instance, AbstractPlayer.PlayerClass chosenClass, ArrayList<String> relics) {

            if (ConfigHelper.applyExtraLevelForAllCharacter && !(__instance instanceof Ines)){
                LogHelper.info("===initializeStarterRelicsPatch:添加遗物===");

                relics.add(ShadowOfLondinium.ID);
            }

        }
    }
}
