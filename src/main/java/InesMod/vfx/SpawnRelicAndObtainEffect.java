package InesMod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class SpawnRelicAndObtainEffect extends AbstractGameEffect {
    public SpawnRelicAndObtainEffect(AbstractRelic relicToObtain){
        this.r = relicToObtain;
        this.startingDuration = duration = Settings.ACTION_DUR_FASTER;
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();

        if (this.duration < 0.0F) {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH/2F,Settings.HEIGHT/2F,r);
            this.isDone = true;
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    AbstractRelic r;
}
