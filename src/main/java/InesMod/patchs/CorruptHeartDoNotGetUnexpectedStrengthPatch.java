package InesMod.patchs;


import InesMod.characters.Ines;
import InesMod.powers.InvisibilityPower;
import InesMod.powers.StrengthStolenPower;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.relics.DeadBranch;
import com.megacrit.cardcrawl.relics.OrangePellets;

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
