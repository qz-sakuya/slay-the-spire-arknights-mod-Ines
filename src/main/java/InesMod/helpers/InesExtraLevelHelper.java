package InesMod.helpers;


import InesMod.dungeons.LevelChapter10;
import InesMod.dungeons.LevelChapter11;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InesExtraLevelHelper
{

    public static boolean isInesExtraLevelID(String id) {
        // 暂时只有一个
        return Objects.equals(id, LevelChapter10.ID);
    }



}
