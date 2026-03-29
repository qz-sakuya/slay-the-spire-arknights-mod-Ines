package InesMod.interpret;


import InesMod.helpers.PathHelper;
import InesMod.truth.TruthManager;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

public class InterpretOption extends AbstractCampfireOption {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(PathHelper.nameToId("InterpretOption"));
    boolean triggered = false;

    public InterpretOption(boolean active) {
        this.label = uiStrings.TEXT[0];
        this.usable = active;
        this.img = ImageMaster.loadImage("InesModResources/img/UI/InterpretButton.png");
        updateUsability(active);
    }

    public void updateUsability(boolean canUse) {
        this.description = canUse ? uiStrings.TEXT[1] : uiStrings.TEXT[2];
    }

    @Override
    public void useOption() {
        if(this.usable) {
            AbstractDungeon.effectList.add(new InterpretEffect(this));  // 解析的实际逻辑
        }
    }

    public float timer = 0.1f;
    public void triggerIt(){
        triggered = true;
        timer = -1F;
    }

    @Override
    public void update() {
        super.update();
        timer -= Gdx.graphics.getDeltaTime();
        if(timer<0F){
            timer = 0.1f; // 每隔 timer 触发一次

            boolean valid = false;
            if(TruthManager.getTotalAmount() > 0){
                valid = true;
            }
            if(triggered){
                valid = false;
            }
            this.usable = valid;
            updateUsability(usable);
        }
    }
}
