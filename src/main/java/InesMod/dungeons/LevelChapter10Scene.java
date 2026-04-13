package InesMod.dungeons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.TrueVictoryRoom;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.scene.ShinySparkleEffect;
import com.megacrit.cardcrawl.vfx.scene.WobblyCircleEffect;
import java.util.ArrayList;
import java.util.Iterator;

public class LevelChapter10Scene extends AbstractScene {
    private ArrayList<AbstractGameEffect> circles = new ArrayList<>();

    protected final TextureAtlas.AtlasRegion boosBg;

    public LevelChapter10Scene() {
        super("InesModResources/img/scenes/chapter10/LevelChapter10.atlas");
        this.ambianceName = "AMBIANCE_BEYOND";
        fadeInAmbiance();

        this.boosBg = this.atlas.findRegion("boosBg");
    }



    public void randomizeScene() {}



    public void nextRoom(AbstractRoom room) {
        super.nextRoom(room);
        randomizeScene();
        if (room instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss) {
            CardCrawlGame.music.silenceBGM();
        }
        fadeInAmbiance();
    }



    public void update(){
        super.update();
        this.updateParticles();
    }



    private void updateParticles() {
        Iterator e = this.circles.iterator();

        while(e.hasNext()) {
            AbstractGameEffect effect = (AbstractGameEffect)e.next();
            effect.update();
            if (effect.isDone) {
                e.remove();
            }
        }

        if (!(AbstractDungeon.getCurrRoom() instanceof TrueVictoryRoom) && this.circles.size() < 72 && !Settings.DISABLE_EFFECTS) {
            if (MathUtils.randomBoolean(0.2F)) {
                this.circles.add(new ShinySparkleEffect());
            } else {
                this.circles.add(new WobblyCircleEffect());
            }
        }

    }

    public void renderCombatRoomBg(SpriteBatch sb) {
        sb.setColor(Color.WHITE);

        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss){
            renderAtlasRegionIf(sb, this.boosBg, true);
        }
        else{
            renderAtlasRegionIf(sb, this.bg, true);
        }


    }

    public void renderCombatRoomFg(SpriteBatch sb) {
        if (!this.isCamp) {
            sb.setBlendFunction(770, 1);

            for (AbstractGameEffect e : this.circles) {
                e.render(sb);
            }

            sb.setBlendFunction(770, 771);
        }

    }

    public void renderCampfireRoom(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        renderAtlasRegionIf(sb, this.campfireBg, true);
    }
}