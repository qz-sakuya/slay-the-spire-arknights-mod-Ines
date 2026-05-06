package InesMod.patchs;


import InesMod.action.EndTurnPatchAction;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;


/**
 * 根据 power 的自定义属性 endOfRoundWhenSkipMonsterTurn，触发一次 atEndOfRound() 回调
 *
 */

public class EndOfRoundWhenSkipMonsterTurnPatch {



    @SpirePatch(clz = AbstractRoom.class,method = "endTurn")
    public static class Fun{
        @SpireInsertPatch(rloc=3)
        public static void Insert(AbstractRoom _inst){
            AbstractDungeon.actionManager.addToBottom(new EndTurnPatchAction(_inst));
        }
    }
}
