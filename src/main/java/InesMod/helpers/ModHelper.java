package InesMod.helpers;

public class ModHelper {
    public static String nameToId(String name) {
        return "InesMod:" + name;
    }
    public static String idToName(String id) {return id.replace("InesMod:","");
    }
}
