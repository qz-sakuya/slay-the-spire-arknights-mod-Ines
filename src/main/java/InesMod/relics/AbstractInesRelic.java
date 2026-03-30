package InesMod.relics;

import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import InesMod.powers.player.StealsPower;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

/**
 * 中文名：破败绣针
 */
public abstract class AbstractInesRelic extends CustomRelic {

    public AbstractInesRelic(String ID,
                             boolean useTmpArt,
                             boolean useOutline,
                             AbstractRelic.RelicTier tier,
                             AbstractRelic.LandingSound sfx) {
        super(ID, "", tier, sfx);

        loadImage(ID, useTmpArt, useOutline);
    }



    // 加载图片
    private void loadImage(String ID, boolean useTmpArt, boolean useOutline) {
        String path = useTmpArt ? getTmpImgPath(false) : getImgPath(ID,false);
        Texture texture = ImageMaster.loadImage(path);

        Texture textureOutline = texture;
        if (useOutline) {
            String pathOutline = useTmpArt ? getTmpImgPath(true) : getImgPath(ID,true);
            textureOutline = ImageMaster.loadImage(pathOutline);
        }

        this.setTextureOutline(texture, textureOutline);
    }


    private static String getTmpImgPath(boolean isOutline) {
        if (!isOutline) {
            return "InesModResources/img/relics/test.png";
        }
        else{
            return "InesModResources/img/relics/test_O.png";
        }

    }

    private static String getImgPath(String id, boolean isOutline) {
        if (!isOutline) {
            return String.format("InesModResources/img/relics/%s.png", PathHelper.idToName(id));
        }
        else{
            return String.format("InesModResources/img/relics/%s_O.png", PathHelper.idToName(id));
        }
    }


    // ===自定义回调===

    public void onAnyMonsterDeath(AbstractMonster monster) {}

    // 进入damage()后，decrementBlock()之前
    public int OnAttackBeforeBlock(DamageInfo info, int damageAmount, int currentBlock) {return damageAmount;}
    public int OnAttackedBeforeBlock(DamageInfo info, int damageAmount, int currentBlock) {return damageAmount;}


    // ===自定义回调end===

    @Override
    public AbstractInesRelic makeCopy() {
        try {
            return getClass().newInstance();
        } catch (IllegalAccessException|InstantiationException var2) {
            throw new RuntimeException("cannot create instance of: " + this.relicId);
        }
    }
}