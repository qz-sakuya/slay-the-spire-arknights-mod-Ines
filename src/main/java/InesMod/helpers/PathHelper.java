package InesMod.helpers;

public class PathHelper {
    public static String nameToId(String name) {
        return "InesMod:" + name;
    }

    public static String idToName(String id) {
        return id.replace("InesMod:","");
    }
}
