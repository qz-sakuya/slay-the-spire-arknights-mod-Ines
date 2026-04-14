package InesMod.event;


import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.relics.FeintTripwire;
import InesMod.relics.UnassumingNeedle;
import InesMod.truth.TruthManager;
import InesMod.truth.TruthReward;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TwilightCorridor extends AbstractImageEvent {
    public static final String ID = PathHelper.nameToId(TwilightCorridor.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private int currentStage;

    private static final int truthAmount = 8;

    public TwilightCorridor() {
        super(NAME, DESCRIPTIONS[0], "InesModResources/img/events/TwilightCorridor/TwilightCorridor_01.png");

        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1], new FeintTripwire());
        this.currentStage = 0;


    }



    protected void buttonEffect(int i) {
        if (this.currentStage == 0) {
            this.imageEventText.clearAllDialogs();
            if (i == 0) {
                this.currentStage = 1;

                // 获得真相
                TruthManager.updateVirtual(truthAmount);

                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.imageEventText.setDialogOption(OPTIONS[2]);
            }
            else {
                this.currentStage = 1;

                // 获得遗物
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(((float) Settings.WIDTH / 2), ((float) Settings.HEIGHT / 2), new FeintTripwire());

                this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                this.imageEventText.setDialogOption(OPTIONS[2]);
            }
        } else {

            openMap();
        }
    }



}