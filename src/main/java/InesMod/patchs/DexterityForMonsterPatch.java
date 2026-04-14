package InesMod.patchs;


import InesMod.characters.Ines;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.powers.monster.DefenseArtilleryMeterPower;
import InesMod.truth.TruthReward;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.relics.DeadBranch;
import com.megacrit.cardcrawl.relics.Kunai;
import com.megacrit.cardcrawl.relics.OrangePellets;
import com.megacrit.cardcrawl.relics.Shuriken;

public class DexterityForMonsterPatch {
    public static final String ID = PathHelper.nameToId("DexterityForMonsterPatch");
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    
    @SpirePatch(clz = AbstractCreature.class,method = "addBlock")
    public static class AddBlockPatch{
        @SpireInsertPatch(rloc = 21)
        public static void Insert(AbstractCreature _inst, int blockAmount){
            if(!_inst.isPlayer){
                LogHelper.info("===DexterityForMonsterPatch：Insert：怪物触发===");

                AbstractPower powerToGet = _inst.getPower(DexterityPower.POWER_ID);
                if (powerToGet != null) {
                    _inst.currentBlock += powerToGet.amount;

                    if (_inst.currentBlock < 0){
                        _inst.currentBlock = 0;
                    }
                }
            }
        }
    }

    // 施加于怪物时，修改描述文本
    // 仅支持该模组支持的语言
    @SpirePatch(clz = DexterityPower.class,method = "updateDescription")
    public static class DescriptionPatch{
        @SpirePostfixPatch
        public static void Postfix(DexterityPower _inst){
            if(!_inst.owner.isPlayer){
                if (_inst.amount > 0) {
                    _inst.description = String.format(uiStrings.TEXT[0], _inst.amount);
                    _inst.type = AbstractPower.PowerType.BUFF;
                } else {
                    int tmp = -_inst.amount;
                    _inst.description = String.format(uiStrings.TEXT[1], tmp);
                    _inst.type = AbstractPower.PowerType.DEBUFF;
                }
            }
        }
    }
}
