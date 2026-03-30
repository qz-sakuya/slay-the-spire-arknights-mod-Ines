package InesMod.powers;

import InesMod.helpers.PathHelper;
import InesMod.modcore.InesModMain;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashMap;
import java.util.Map;


public abstract class AbstractInesPower extends AbstractPower {
    // 使用Map来存储已加载的图片资源
    private static final Map<String, TextureAtlas.AtlasRegion> powerImgCache = new HashMap<>();
    public String[] descriptions;

    // 可选的第二个数字
    public Integer secondAmount = null;
    final Color redColor = new Color(1.0F, 0.0F, 0.0F, 1.0F);
    final Color greenColor = new Color(0.0F, 1.0F, 0.0F, 1.0F);

    public AbstractInesPower(String ID, boolean useTmpArt, PowerStrings strings, AbstractCreature owner, PowerType type, int amount, Integer secondAmount) {
        this.ID = ID;
        this.name = strings.NAME;
        this.owner = owner;
        this.type = type;
        this.amount = amount; // -1为不可叠加
        this.secondAmount = secondAmount;
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
    public AbstractInesPower(String ID, boolean useTmpArt, PowerStrings strings, AbstractCreature owner, PowerType type, int amount) {
        this(ID, useTmpArt, strings, owner, type, amount, null);
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
        return String.format("InesModResources/img/powers/%s%s.png", PathHelper.idToName(id),size);
    }


    // ===自定义回调===
    public void onCardMove(AbstractCard c, CardGroup.CardGroupType groupType) {}

    // 补充一个手动弃牌时的回调，与遗物的同名
    public void onManualDiscard(AbstractCard c){}

    // 战斗中有新的怪物生成
    public void onSpawnMonster(AbstractMonster monster) {}

    // 战斗中有怪物死亡
    public void onAnyMonsterDeath(AbstractMonster monster) {}

    // 进入damage()后，decrementBlock()之前
    public int OnAttackBeforeBlock(DamageInfo info, int damageAmount, int currentBlock) {return damageAmount;}
    public int OnAttackedBeforeBlock(DamageInfo info, int damageAmount, int currentBlock) {return damageAmount;}

    // ===自定义回调end===

//    // 绘制第二个数字
//    public void renderSecondAmount(SpriteBatch sb, float x, float y, Color c) {
//        if (secondAmount == null) {
//            return;
//        }
//
//        // LoggerHelper.info("===AbstractInesPower renderSecondAmount：secondAmount层数：{}===",secondAmount);
//
//        /*
//        if (this.secondAmount > 0) {
//            if (!this.isTurnBased) {
//                this.greenColor.a = c.a;
//                c = this.greenColor;
//            }
//
//            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.secondAmount), x, y, this.fontScale, c);
//        } else if (this.secondAmount < 0) {
//            this.redColor.a = c.a;
//            c = this.redColor;
//            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.secondAmount), x, y, this.fontScale, c);
//        }
//         */
//
//        // 默认白色
//        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.secondAmount), x, y, this.fontScale, c);
//
//    }

    @Override
    // 绘制第二个数字 另一种实现（参考stslib）
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);

        if (secondAmount != null) {
            // 默认白色
            FontHelper.renderFontRightTopAligned(sb,
                    FontHelper.powerAmountFont,
                    Integer.toString(this.secondAmount),
                    x,
                    y + 15.0F * Settings.scale,
                    this.fontScale,
                    c);
        }
    }
}