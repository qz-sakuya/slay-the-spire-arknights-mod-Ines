package InesMod.save;

import InesMod.truth.TruthManager;


public class InesSave {
    public int truthAmount;
    public InesSave(){}

    @Override
    public String toString() {
        return "InesSave{" +
                "truthAmount=" + truthAmount +
                '}';
    }


    public void onSave(){
        truthAmount = TruthManager.amount;
    }

    public void onLoad(){
        TruthManager.amount = truthAmount;
        TruthManager.clearVirtual();
    }

    public void onDelete(){
        TruthManager.amount = 0;
        TruthManager.clearVirtual();
    }
}
