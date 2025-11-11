package InesMod.powers;

import InesMod.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashMap;
import java.util.Map;


public abstract class AbstractInesPower extends AbstractPower {
    // 使用Map来存储已加载的图片资源
    private static final Map<String, TextureAtlas.AtlasRegion> powerImgCache = new HashMap<>();
    public String[] descriptions;

    public AbstractInesPower(String ID, boolean useTmpArt, PowerStrings strings, AbstractCreature owner, PowerType type, int amount) {
        this.ID = ID;
        this.name = strings.NAME;
        this.owner = owner;
        this.type = type;
        this.amount = amount; // -1为不可叠加
        this.descriptions = strings.DESCRIPTIONS;

        if (!powerImgCache.containsKey(ID)) {
            // 如果当前ID对应的图片未被加载，则进行加载并缓存
            loadAndCacheImage(ID, useTmpArt);
        }

        // 从缓存中获取对应ID的图片
        this.region128 = powerImgCache.get(ID + "_128");
        this.region48 = powerImgCache.get(ID + "_48");

        // 首次添加能力更新描述
        this.updateDescription();
    }


    // 加载图片并放入缓存
    private void loadAndCacheImage(String ID, boolean useTmpArt) {
        String path128 = useTmpArt ? getTmpImgPath("84") : getImgPath("84", ID);
        String path48 = useTmpArt ? getTmpImgPath("32") : getImgPath("32", ID);

        powerImgCache.put(ID + "_128", new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84));
        powerImgCache.put(ID + "_48", new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32));
    }


    public abstract void updateDescription();


    public AbstractInesPower makeCopy() {
        try {
            return getClass().newInstance();
        } catch (IllegalAccessException|InstantiationException var2) {
            throw new RuntimeException("cannot create instance of: " + this.ID);
        }
    }

    private static String getTmpImgPath(String size) {
        return String.format("InesModResources/img/powers/testPower%s.png",size);
    }

    private static String getImgPath(String size, String id) {
        return String.format("InesModResources/img/powers/%s%s.png",ModHelper.idToName(id),size);
    }

    // 自定义回调
    public void onCardMove() {}

}