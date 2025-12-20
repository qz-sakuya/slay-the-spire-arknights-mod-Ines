package InesMod.truth;

import InesMod.helpers.PathHelper;
import basemod.TopPanelItem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

public class TruthTopItem extends TopPanelItem {
    public static final String ID = PathHelper.nameToId("TruthTopItem");
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);



    private static final float INFO_TEXT_Y = Settings.isMobile ? (float)Settings.HEIGHT - 36.0F * Settings.scale : (float)Settings.HEIGHT - 24.0F * Settings.scale;

    public TruthTopItem(){
        super(new TextureAtlas.AtlasRegion(new Texture("InesModResources/img/UI/TruthItem.png"),0,0,64,64).getTexture(),
                ID);
    }


    boolean renderTip = false;

    @Override
    protected void onClick() {

    }

    @Override
    public void render(SpriteBatch sb, Color color) {
        super.render(sb,color);
        FontHelper.renderFontLeftTopAligned(sb,FontHelper.topPanelAmountFont,Integer.toString(TruthManager.truthAmount), this.x + 64F* Settings.scale, INFO_TEXT_Y,Settings.CREAM_COLOR);
        if(renderTip){
            sb.setColor(Color.WHITE);
            TipHelper.renderGenericTip(this.x,this.y,uiStrings.TEXT[0],uiStrings.TEXT[1]);
        }
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    @Override
    protected void onHover() {
        super.onHover();
        renderTip = true;
    }

    @Override
    protected void onUnhover() {
        super.onUnhover();
        renderTip = false;
    }
}
