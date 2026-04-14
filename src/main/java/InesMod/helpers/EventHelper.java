package InesMod.helpers;

import InesMod.event.TwilightCorridor;
import basemod.BaseMod;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;

public class EventHelper {


    public static void initializeEvents() {
        BaseMod.addEvent((new AddEventParams.Builder(TwilightCorridor.ID, TwilightCorridor.class))
                .eventType(EventUtils.EventType.ONE_TIME)
                .dungeonID("TheCity")
                .dungeonID("TheBeyond")
                .endsWithRewardsUI(false)
                .create()
        );
    }
}
