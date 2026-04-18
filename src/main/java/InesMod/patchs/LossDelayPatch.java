package InesMod.patchs;


import InesMod.action.DelayToAddAction;
import InesMod.action.FlashPowerAction;
import InesMod.characters.Ines;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.DeadBranch;
import com.megacrit.cardcrawl.relics.Kunai;
import com.megacrit.cardcrawl.relics.OrangePellets;
import com.megacrit.cardcrawl.relics.Shuriken;

/**
 * 使 敏捷下降 延迟结算
 * 使影哨可以正常吃到临时敏捷
 * 把 活动肌肉 一块改了统一一点
 */
public class LossDelayPatch {


    @SpirePatch(clz = LoseDexterityPower.class,method = "atEndOfTurn")
    public static class Fun1{
        @SpireInsertPatch(rloc=0)
        public static SpireReturn<Void> Insert(LoseDexterityPower _inst, boolean isPlayer){
            AbstractDungeon.actionManager.addToBottom(new DelayToAddAction(new FlashPowerAction(_inst)));
            AbstractDungeon.actionManager.addToBottom(new DelayToAddAction(new ApplyPowerAction(_inst.owner, _inst.owner, new DexterityPower(_inst.owner, -_inst.amount), -_inst.amount)));
            AbstractDungeon.actionManager.addToBottom(new DelayToAddAction(new RemoveSpecificPowerAction(_inst.owner, _inst.owner, "DexLoss")));
            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = LoseStrengthPower.class,method = "atEndOfTurn")
    public static class Fun2{
        @SpireInsertPatch(rloc=0)
        public static SpireReturn<Void> Insert(LoseStrengthPower _inst, boolean isPlayer){
            AbstractDungeon.actionManager.addToBottom(new DelayToAddAction(new FlashPowerAction(_inst)));
            AbstractDungeon.actionManager.addToBottom(new DelayToAddAction(new ApplyPowerAction(_inst.owner, _inst.owner, new StrengthPower(_inst.owner, -_inst.amount), -_inst.amount)));
            AbstractDungeon.actionManager.addToBottom(new DelayToAddAction(new RemoveSpecificPowerAction(_inst.owner, _inst.owner, "Flex")));
            return SpireReturn.Return();
        }
    }
}
