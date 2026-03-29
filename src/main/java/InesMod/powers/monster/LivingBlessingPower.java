package InesMod.powers.monster;

import InesMod.action.ApplyNonStackPowerAction;
import InesMod.action.ForceWaitAction;
import InesMod.action.MoveMonsterToIndexAction;
import InesMod.action.ShowHealthBarAction;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.monsters.Chapter10.TouchOfSanguinarch;
import InesMod.monsters.Chapter10.GiftOfSanguinarch;
import InesMod.patchs.OnSpawnMonsterPatch;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

/**
 * 中文名：活体赐福
 * 英文名：Living Blessing
 * 敌方power
 * 图标
 * 死亡后召唤重生造物/大号重生造物
 */
public class LivingBlessingPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(LivingBlessingPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    private boolean spawnElite = false;

    public LivingBlessingPower(AbstractCreature owner, int amount, boolean spawnElite) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount); // 不可叠加
        this.spawnElite = spawnElite;

        updateDescription();
    }


    @Override // 怪物是否触发需要测试
    public void onDeath() {
        if (this.owner.isDying){
            LogHelper.info("===LivingBlessingPower: onDeath， spawnElite：{}===",this.spawnElite);
            ((AbstractMonster) this.owner).deathTimer -= 0.5F;

            AbstractMonster newMonster;
            if(!spawnElite){
                newMonster = new TouchOfSanguinarch(0,0);
            }
            else{
                newMonster = new GiftOfSanguinarch(0,0);
            }

            // 修正位置
            newMonster.drawX = this.owner.drawX;
            newMonster.drawY = this.owner.drawY;

            // ===直接生成===
            // 参考 SpawnMonsterAction
            for(AbstractRelic r : AbstractDungeon.player.relics) {
                r.onSpawnMonster(newMonster);
            }

            newMonster.init();
            newMonster.applyPowers();
            newMonster.hideHealthBar(); // 暂时隐藏

            // 记录原位置
            int targetIndex = AbstractDungeon.getCurrRoom().monsters.monsters.indexOf((AbstractMonster) this.owner);

            // 先放到列表末尾，后续再修改
            // 避免对群攻事件造成影响
            int sourceIndex = AbstractDungeon.getCurrRoom().monsters.monsters.size();
            AbstractDungeon.getCurrRoom().monsters.addMonster(sourceIndex, newMonster);

            if (ModHelper.isModEnabled("Lethality")) {
                this.addToBot(new ApplyPowerAction(newMonster, newMonster, new StrengthPower(newMonster, 3), 3));
            }

            if (ModHelper.isModEnabled("Time Dilation")) {
                this.addToBot(new ApplyPowerAction(newMonster, newMonster, new SlowPower(newMonster, 0)));
            }

            // 通知patch
            OnSpawnMonsterPatch.Work(newMonster);

            // ===直接生成End===


            // 新怪物执行初始化
            addToBot(new ChangeStateAction(newMonster, "START"));
            addToBot(new ApplyNonStackPowerAction(newMonster, newMonster, new RebornCreationPower(newMonster, -1)));
            addToBot(new ForceWaitAction(0.3F));
            addToBot(new ShowHealthBarAction(newMonster));
            addToBot(new MoveMonsterToIndexAction(newMonster, targetIndex));
        }
    }

    @Override
    public void updateDescription() {
        if(!spawnElite) {
            this.description = String.format(descriptions[0]);
        }
        else {
            this.description = String.format(descriptions[1]);
        }
    }
}

