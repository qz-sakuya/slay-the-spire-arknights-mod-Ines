package InesMod.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.exceptions.SaveFileLoadError;
import com.megacrit.cardcrawl.helpers.AsyncSaver;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.saveAndContinue.SaveFileObfuscator;

import java.io.File;

public class InesSaveAndContinue {
    public static void saveInes(InesSave inesSave){
        Gson gson = new Gson();
        String data = gson.toJson(inesSave);
        String filepath = getInesSavePath(AbstractDungeon.player.chosenClass);
        if(Settings.isBeta){
            AsyncSaver.save(filepath + "BETA",data);
        }
        AsyncSaver.save(filepath, SaveFileObfuscator.encode(data,"key"));
    }

    public static InesSave loadInes(AbstractPlayer.PlayerClass c){
        String fileName = getInesSavePath(c);

        try {
            return loadInes(fileName);
        } catch (SaveFileLoadError var3) {
            return null;
        }
    }

    public static void deleteInes(AbstractPlayer.PlayerClass c){
        String savePath = getInesSavePath(c);
        Gdx.files.local(savePath).delete();
        Gdx.files.local(savePath + ".backUp").delete();
    }



    private static InesSave loadInes(String filePath) throws SaveFileLoadError{
        InesSave saveFile = null;
        Gson gson = new Gson();
        String savestr = null;
        Exception err = null;

        try {
            savestr = loadSaveString(filePath);
            saveFile = gson.fromJson(savestr, InesSave.class);
        } catch (Exception var6) {
            if (Gdx.files.local(filePath).exists()) {
                SaveHelper.preserveCorruptFile(filePath);
            }

            err = var6;
            if (!filePath.endsWith(".backUp")) {
                return loadInes(filePath + ".backUp");
            }
        }

        if (saveFile == null) {
            throw new SaveFileLoadError("Didn't Find Right Now: " + filePath, err);
        } else {
            return saveFile;
        }
    }

    private static String loadSaveString(String filePath) {
        FileHandle file = Gdx.files.local(filePath);
        String data = file.readString();
        return SaveFileObfuscator.isObfuscated(data) ? SaveFileObfuscator.decode(data, "key") : data;
    }

    private static String getInesSavePath(AbstractPlayer.PlayerClass c){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("saves").append(File.separator).append("InesMod").append(File.separator);
        if (CardCrawlGame.saveSlot != 0) {
            stringBuilder.append(CardCrawlGame.saveSlot).append("_");
        }

        //WAVE 把这里改成你自己mod就行
        stringBuilder.append(c.name()).append(".autosave");
        return stringBuilder.toString();
    }
}
