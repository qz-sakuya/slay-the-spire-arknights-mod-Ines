package InesMod.truth;


import InesMod.enums.OtherEnum;
import InesMod.helpers.PathHelper;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

public class TruthReward extends CustomReward {
    public static final String ID = PathHelper.nameToId(TruthReward.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);


    public int amount;

    public TruthReward(int amount, boolean FromInsight) {
        super(new TextureAtlas.AtlasRegion(new Texture("InesModResources/img/UI/TruthItem.png"),0,0,64,64).getTexture(),
                null,
                OtherEnum.INES_TRUTH);
        this.text = amount + uiStrings.TEXT[0];
        if (FromInsight) {
            this.text += uiStrings.TEXT[1];
        }

        this.amount = amount;
    }

    @Override
    public boolean claimReward() {
        // 不直接获得，而是获得虚值
        TruthManager.updateVirtual(amount);
        return true;
    }
}
