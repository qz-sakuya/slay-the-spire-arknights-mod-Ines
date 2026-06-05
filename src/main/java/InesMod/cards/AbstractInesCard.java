package InesMod.cards;

import InesMod.helpers.PathHelper;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.util.ArrayList;


// 笔记：-2费不显示能量图标（如诅咒卡状态卡等），-1费为X费。



public abstract class AbstractInesCard extends CustomCard {
    public int consumeSteals; // 此牌消耗偷取的层数
    public boolean dontUseAttackAnimation; // 禁用自动攻击动画

    public boolean isAutoUse = false; // 是否启用自动打出

    public CardGroup.CardGroupType lastAddedTo; // 上次加入到的 CardGroup
    public boolean addedFromSameGroup; // 上次移动时，是否在相同 CardGroup 间移动

    // 动态预览 的预览卡列表，不为空则启用该机制
    // 该机制会覆盖常规 this.cardToPreview
    public final ArrayList<AbstractCard> cardToPreviewList = new ArrayList<>();
    public float cardToPreviewRotationTime = 2.5F; // 动态预览的切换时间

    // 动态预览 的辅助变量
    private float previewRotationTimer;
    private int previewIndex = 0;


    public AbstractInesCard(String ID,
                            boolean useTmpArt,
                            CardStrings strings,
                            int COST,
                            CardType TYPE,
                            CardRarity RARITY,
                            CardTarget TARGET,
                            CardColor color) {
        super(ID, strings.NAME, useTmpArt ? getTmpImgPath(TYPE) : getImgPath(TYPE, ID), COST, strings.DESCRIPTION, TYPE,
                color, RARITY, TARGET);

        consumeSteals = 1;
        dontUseAttackAnimation = false;
    }


    private static String getImgPath(CardType t, String id) {
        String type;
        switch (t) {
            case ATTACK:
                type = "attack";
                break;
            case POWER:
                type = "power";
                break;
            case STATUS:
                type = "status";
                break;
            case CURSE:
                type = "curse";
                break;
            case SKILL:
                type = "skill";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + t);
        }
        return String.format("InesModResources/img/cards/%s/%s_%s.png", type, PathHelper.idToName(id), type);
    }

    private static String getTmpImgPath(CardType t) {
        String type;
        switch (t) {
            case ATTACK:
                type = "attack";
                break;
            case POWER:
                type = "power";
                break;
            case STATUS:
            case CURSE:
            case SKILL:
                type = "skill";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + t);
        }
        return String.format("InesModResources/img/cards/test/test_%s.png", type);
    }








    // 自定义回调

    // 战斗开始
    public void atBattleStartPreDraw() {}

    // 战斗结束
    public void atBattleEnd() {}


    // 任何卡被打出
    public void onReceiveCardUsed(AbstractCard c) {}

    // 任何卡被消耗（监测moveToExhaustPile）
    public void onExhaust(AbstractCard c) {}

    // 任何卡被丢弃（监测moveToDiscardPile）
    // public void onDiscard(AbstractCard c) {}

    // 任何卡被手动丢弃（监测incrementDiscard）
    public void onManualDiscard(AbstractCard c){}

    // 任何卡被移动（监测所有addTo类似函数，比moveTo更底层）
    public void onCardMove(AbstractCard c, CardGroup.CardGroupType groupType) {}

    /*
    例如，MakeTempCardInDiscardAction 会使用 addTo 但不使用 moveTo
     */

    // 自身自动打出失败
    public void triggerOnAutoUseFail() {}



    @Override
    public void update() {
        super.update();

        // 处理动态预览列表
        if (!this.cardToPreviewList.isEmpty() && AbstractDungeon.actionManager.isEmpty() &&
                this.hb.hovered) {
            if (this.previewRotationTimer <= 0.0F) {
                this.previewRotationTimer = cardToPreviewRotationTime;
                if (this.previewIndex == this.cardToPreviewList.size() - 1) {
                    this.previewIndex = 0;
                } else {
                    this.previewIndex++;
                }
                if (this.previewIndex >= this.cardToPreviewList.size()) {
                    this.previewIndex = this.cardToPreviewList.size() - 1;
                }
                this.cardsToPreview = this.cardToPreviewList.get(this.previewIndex);
            } else {
                this.previewRotationTimer -= Gdx.graphics.getDeltaTime();
            }
        }
    }
}