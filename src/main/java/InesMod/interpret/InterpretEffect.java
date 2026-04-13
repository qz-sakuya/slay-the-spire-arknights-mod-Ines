package InesMod.interpret;


import InesMod.cards.AbstractInesUICard;
import InesMod.cards.ui.*;
import InesMod.helpers.ConfigHelper;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.relics.ShadowOfLondinium;
import InesMod.truth.TruthManager;
import InesMod.vfx.SpawnRelicAndObtainEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * 解析真相的实际逻辑
 * 暂未加入结局遗物的事件
 */
public class InterpretEffect extends AbstractGameEffect {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(PathHelper.nameToId("InterpretEffect"));
    public static final String[] TEXT = uiStrings.TEXT;
    private static final float DUR = 1.5F;
    private final Color screenColor;

    private final InterpretOption InterpretOption;

    InterpretPhase currentPhase;

    ArrayList<AbstractCard> UICardList;

    public static final ArrayList<String> specialRelics = new ArrayList<>();


    public InterpretEffect(InterpretOption option) {
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.duration = DUR;
        this.screenColor.a = 0.0F;
        this.InterpretOption = option;
        AbstractDungeon.overlayMenu.proceedButton.hide();
        currentPhase = InterpretPhase.START;
    }

    @Override
    public void update() {
        if (!AbstractDungeon.isScreenUp) {
            duration -= Gdx.graphics.getDeltaTime();
            updateBlackScreenColor();
        }

        // 添加初始选项
        if (currentPhase == InterpretPhase.START) {
            ArrayList<AbstractCard> options = new ArrayList<>();
            options.add(new InterpretReturn());
            options.add(new InterpretColorlessCard());
            options.add(new InterpretRareCard());
            options.add(new InterpretRelic());

            // 未禁用结局，且未拥有结局遗物，才出现结局选项
            if (!ConfigHelper.banExtraLevel){
                boolean hasEndingRelic = false;
                for(AbstractRelic r : AbstractDungeon.player.relics){
                    if(r.relicId.equals(ShadowOfLondinium.ID)) {
                        hasEndingRelic = true;
                        break;
                    }
                }

                if(!hasEndingRelic){
                    options.add(new InterpretSecret());
                }
            }




            for (AbstractCard c : options) {
                ((AbstractInesUICard)c).JudgeAvailability();
            }

            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            tmp.group = options;
            AbstractDungeon.gridSelectScreen.open(tmp, 1, TEXT[0], false, false, true, false);
            currentPhase = InterpretPhase.OPTION;
        }
        else if (currentPhase == InterpretPhase.OPTION) {
            // 如果选择了选项
            if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (AbstractCard OptionCard : AbstractDungeon.gridSelectScreen.selectedCards) {
                    if (OptionCard instanceof AbstractInesUICard && !((AbstractInesUICard)OptionCard).available) {
                        // 选项不满足要求，则结束
                        this.duration = 0.1F;
                        currentPhase = InterpretPhase.END;
                        break;
                    }
                    else if (OptionCard instanceof InterpretColorlessCard) {  // 选项：无色卡牌
                        ArrayList<AbstractCard> options = new ArrayList<>();

                        // 生成随机无色牌
                        ArrayList<AbstractCard> rare = new ArrayList<>(AbstractDungeon.colorlessCardPool.group);
                        Collections.shuffle(rare, AbstractDungeon.cardRandomRng.random);
                        for (int i = 0; i < 3; i++) {
                            if (!rare.isEmpty()) {
                                options.add(rare.remove(0));
                            }
                        }

                        if (!options.isEmpty()) {
                            currentPhase = InterpretPhase.CARD_SELECT;
                            InterpretOption.triggerIt();
                            TruthManager.updateVirtual(-OptionCard.magicNumber);
                            AbstractDungeon.cardRewardScreen.customCombatOpen(options, TEXT[1], true);
                            this.duration = 0.1F; // 缩短，使得新Screen锁duration后，skip奖励时不会卡太久
                        } else {
                            this.duration = 0.1F;
                            currentPhase = InterpretPhase.END;
                        }
                    }
                    else if (OptionCard instanceof InterpretRareCard) {  // 选项：稀有卡牌
                        ArrayList<AbstractCard> options = new ArrayList<>();

                        // 生成随机稀有牌
                        ArrayList<AbstractCard> rare = new ArrayList<>(AbstractDungeon.rareCardPool.group);
                        Collections.shuffle(rare, AbstractDungeon.cardRandomRng.random);
                        for (int i = 0; i < 3; i++) {
                            if (!rare.isEmpty()) {
                                options.add(rare.remove(0));
                            }
                        }

                        if (!options.isEmpty()) {
                            currentPhase = InterpretPhase.CARD_SELECT;
                            InterpretOption.triggerIt();
                            TruthManager.updateVirtual(-OptionCard.magicNumber);
                            AbstractDungeon.cardRewardScreen.customCombatOpen(options, TEXT[2], true);
                            this.duration = 0.1F;
                        } else {
                            this.duration = 0.1F;
                            currentPhase = InterpretPhase.END;
                        }
                    }
                    else if (OptionCard instanceof InterpretRelic) { // 选项：遗物
                        ArrayList<AbstractCard> options = new ArrayList<>();

                        // 生成随机遗物
                        ArrayList<AbstractRelic> relics = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            // 随机稀有度
                            AbstractRelic tmpRelic = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
                            relics.add(tmpRelic);
                        }

                        // 生成对应ui牌
                        for (AbstractRelic rt : relics) {
                            RelicPreviewCard previewCard = new RelicPreviewCard();
                            previewCard.setRelic(rt);
                            previewCard.setTotalRelics(relics);
                            options.add(previewCard);
                        }

                        currentPhase = InterpretPhase.RELIC_SELECT;
                        InterpretOption.triggerIt(); // 使该篝火选项失效
                        TruthManager.updateVirtual(-OptionCard.magicNumber); // 扣除对应真相
                        AbstractDungeon.cardRewardScreen.customCombatOpen(options, TEXT[3], true);
                        this.duration = 0.1F;
                    }
                    else if (OptionCard instanceof InterpretSecret) { // 选项：结局
                        // 生成并获得结局遗物
                        AbstractRelic r = new ShadowOfLondinium();
                        AbstractDungeon.topLevelEffectsQueue.add(new SpawnRelicAndObtainEffect(r));

                        currentPhase = InterpretPhase.END;
                        InterpretOption.triggerIt(); // 使该篝火选项失效
                        TruthManager.updateVirtual(-OptionCard.magicNumber); // 扣除对应真相
                        this.duration = 0.1F;
                    }
                    else {  //选项：返回
                        this.duration = 0.1F;
                        currentPhase = InterpretPhase.END;
                    }

                    break;
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
            }
        }
        else if(currentPhase == InterpretPhase.CARD_SELECT){
            // LogHelper.info("===InterpretEffect CARD_SELECT：当前duration = {}",duration);
            if(AbstractDungeon.cardRewardScreen.discoveryCard !=null){
                // LogHelper.info("===InterpretEffect CARD_SELECT：已选择奖励");
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(AbstractDungeon.cardRewardScreen.discoveryCard.makeCopy(),Settings.WIDTH/2F,Settings.HEIGHT/2F));
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
                currentPhase = InterpretPhase.END;
                duration = 0.1F;
            }
        }
        else if(currentPhase == InterpretPhase.RELIC_SELECT){
            if(AbstractDungeon.cardRewardScreen.discoveryCard !=null){
                AbstractDungeon.cardRewardScreen.discoveryCard.onChoseThisOption();
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
                currentPhase = InterpretPhase.END;
                duration = 0.1F;
            }
        }




        if (this.duration < 0F) {
            // LogHelper.info("===InterpretEffect CARD_SELECT：duration < 0F，结束");
            this.isDone = true;
            ((RestRoom) AbstractDungeon.getCurrRoom()).campfireUI.reopen();
        }
    }

    private void updateBlackScreenColor(){
        if (this.duration > 1.0F) {
            this.screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - 1.0F) * 2.0F);
        } else {
            this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / 1.5F);
        }
    }


    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float) Settings.WIDTH, (float)Settings.HEIGHT);
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID) {
            AbstractDungeon.gridSelectScreen.render(sb);
        }
        else if(AbstractDungeon.screen == AbstractDungeon.CurrentScreen.CARD_REWARD){
            AbstractDungeon.cardRewardScreen.render(sb);
        }
    }

    @Override
    public void dispose() {

    }

    public enum InterpretPhase {
        START,
        OPTION,
        CARD_SELECT,
        RELIC_SELECT,
        END
    }
}
