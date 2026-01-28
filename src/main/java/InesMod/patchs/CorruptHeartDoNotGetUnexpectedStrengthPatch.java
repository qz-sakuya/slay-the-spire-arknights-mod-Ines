package InesMod.patchs;


import InesMod.powers.player.StrengthStolenPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;

/**
 * 如果心脏强化回合获得了力量，则同步删除对应量的 被偷取力量 power
 */
public class CorruptHeartDoNotGetUnexpectedStrengthPatch {
    @SpirePatch(clz = CorruptHeart.class,method = "takeTurn")
    public static class Fun{
        @SpirePrefixPatch
        public static void Prefix(CorruptHeart _inst){
            if(_inst.nextMove == 4){
                int additionalAmount = 0;
                if (_inst.hasPower("Strength") && (_inst.getPower("Strength")).amount < 0) {
                    additionalAmount = -(_inst.getPower("Strength")).amount;
                }

                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(_inst, _inst, StrengthStolenPower.ID, additionalAmount));
            }
        }
    }
}
