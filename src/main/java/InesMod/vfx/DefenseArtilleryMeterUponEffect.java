package InesMod.vfx;


import InesMod.action.ForceWaitAction;
import InesMod.characters.Ines;
import InesMod.helpers.LogHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class DefenseArtilleryMeterUponEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private TextureAtlas.AtlasRegion img;

    public DefenseArtilleryMeterUponEffect(int type, float x, float y, float scale) {
        String url = String.format("InesModResources/img/vfx/DefenseArtilleryMeterUpon/DefenseArtilleryMeterUpon%d.png", type);
        img = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(url), 0, 0, 3000, 100);


        this.x = x - (float)this.img.packedWidth / 2.0F; // 修正坐标以居中
        this.y = y - (float)this.img.packedHeight / 2.0F;

//        this.x = x - (float)this.img.packedWidth / 2.0F * scale; // 修正坐标以居中
//        this.y = y - (float)this.img.packedHeight / 2.0F * scale;

        this.scale = scale;
        this.color = com.badlogic.gdx.graphics.Color.WHITE.cpy(); // 使用白色以保持原图颜色
        this.color.a = 1.0f; // 完全不透明


        this.duration = 99999.0F;
        this.renderBehind = false; // 在角色图层上方渲染
        this.isDone = false;
    }



    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        // 使用标准混合模式
        sb.setBlendFunction(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA);

        // 绘制图像
        sb.draw(this.img, this.x, this.y,
                (float)this.img.packedWidth / 2.0F,
                (float)this.img.packedHeight / 2.0F,
                (float)this.img.packedWidth,
                (float)this.img.packedHeight,
                this.scale, this.scale, this.rotation);

        // 恢复混合模式
        sb.setBlendFunction(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA);
    }



    @Override
    public void dispose() {}
}