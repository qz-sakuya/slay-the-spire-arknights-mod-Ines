package InesMod.patchs;


import InesMod.action.EndTurnPatchAction;
import InesMod.characters.Ines;
import InesMod.helpers.LogHelper;
import InesMod.powers.AbstractInesPower;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;


/**
 * 如果 power 有 endOfRoundWhenSkipMonsterTurn 则触发
 */

public class EndTurnPatch {



    @SpirePatch(clz = AbstractRoom.class,method = "endTurn")
    public static class Fun{
        @SpireInsertPatch(rloc=3)
        public static void Insert(AbstractRoom _inst){
            AbstractDungeon.actionManager.addToBottom(new EndTurnPatchAction(_inst));
        }
    }
}
