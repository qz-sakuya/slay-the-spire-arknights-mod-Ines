package InesMod.save;

import InesMod.patchs.ExtraLevelPatch;
import InesMod.truth.TruthManager;



public class InesSave {
    public int truthAmount;

    public boolean enterChapter10 = false;
    public boolean enteredChapter10 = false;

    public boolean enterChapter11 = false;
    public boolean enteredChapter11 = false;

    public boolean enterChapter12 = false;
    public boolean enteredChapter12 = false;

    public boolean enterChapter13 = false;
    public boolean enteredChapter13 = false;


    public InesSave(){}

    @Override
    public String toString() {
        return "InesSave{" +
                "truthAmount=" + truthAmount +
                ", EnterChapter10 =" + enterChapter10 +
                ", EnteredChapter10=" + enteredChapter10 +
                ", EnterChapter11 =" + enterChapter11 +
                ", EnteredChapter11=" + enteredChapter11 +
                ", EnterChapter12 =" + enterChapter12 +
                ", EnteredChapter12=" + enteredChapter12 +
                ", EnterChapter13 =" + enterChapter13 +
                ", EnteredChapter13=" + enteredChapter13 +
                '}';
    }


    public void onSave(){
        truthAmount = TruthManager.amount;

        enterChapter10 = ExtraLevelPatch.EnterChapter10;
        enteredChapter10 = ExtraLevelPatch.EnteredChapter10;
        enterChapter11 = ExtraLevelPatch.EnterChapter11;
        enteredChapter11 = ExtraLevelPatch.EnteredChapter11;
        enterChapter12 = ExtraLevelPatch.EnterChapter12;
        enteredChapter12 = ExtraLevelPatch.EnteredChapter12;
        enterChapter13 = ExtraLevelPatch.EnterChapter13;
        enteredChapter13 = ExtraLevelPatch.EnteredChapter13;

    }

    public void onLoad(){
        TruthManager.amount = truthAmount;
        TruthManager.clearVirtual();

        ExtraLevelPatch.EnterChapter10 = enterChapter10;
        ExtraLevelPatch.EnteredChapter10 = enteredChapter10;
        ExtraLevelPatch.EnterChapter11 = enterChapter11;
        ExtraLevelPatch.EnteredChapter11 = enteredChapter11;
        ExtraLevelPatch.EnterChapter12 = enterChapter12;
        ExtraLevelPatch.EnteredChapter12 = enteredChapter12;
        ExtraLevelPatch.EnterChapter13 = enterChapter13;
        ExtraLevelPatch.EnteredChapter13 = enteredChapter13;
    }

    public void onDelete(){
        TruthManager.amount = 0;
        TruthManager.clearVirtual();

        ExtraLevelPatch.EnterChapter10 = false;
        ExtraLevelPatch.EnteredChapter10 = false;
        ExtraLevelPatch.EnterChapter11 = false;
        ExtraLevelPatch.EnteredChapter11 = false;
        ExtraLevelPatch.EnterChapter12 = false;
        ExtraLevelPatch.EnteredChapter12 = false;
        ExtraLevelPatch.EnterChapter13 = false;
        ExtraLevelPatch.EnteredChapter13 = false;
    }
}
