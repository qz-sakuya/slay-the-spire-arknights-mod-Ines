package InesMod.patchs;


import InesMod.save.InesSave;
import InesMod.save.InesSaveAndContinue;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

public class SaveAndContinuePatch {
    public static InesSave inesSave = new InesSave();

    @SpirePatch(clz = SaveAndContinue.class, method = "save")
    public static class SavePatch {
        @SpirePostfixPatch
        public static void Postfix(SaveFile save) {
            inesSave.onSave();
            InesSaveAndContinue.saveInes(inesSave);
        }
    }

    @SpirePatch(clz = SaveAndContinue.class, method = "loadSaveFile",paramtypez = {AbstractPlayer.PlayerClass.class})
    public static class LoadPatch {
        @SpirePostfixPatch
        public static SaveFile Postfix(SaveFile _ret, AbstractPlayer.PlayerClass c) {
            InesSave save = InesSaveAndContinue.loadInes(c);
            if(save!=null){
                inesSave = save;
                inesSave.onLoad();
            }
            return _ret;
        }
    }

    @SpirePatch(clz = SaveAndContinue.class,method = "deleteSave")
    public static class DeletePatch{
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer p){
            inesSave.onDelete();
            InesSaveAndContinue.deleteInes(p.chosenClass);
        }
    }
}
