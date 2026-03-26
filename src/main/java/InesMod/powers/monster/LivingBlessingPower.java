package InesMod.powers.monster;

import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.monsters.Chapter10.TouchOfSanguinarch;
import InesMod.monsters.Chapter10.GiftOfSanguinarch;
import InesMod.powers.AbstractInesPower;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

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
                true,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount); // 不可叠加
        this.spawnElite = spawnElite;
    }


    @Override // 怪物是否触发需要测试
    public void onDeath() {
        if (this.owner.isDying){
            LogHelper.info("===LivingBlessingPower: onDeath， spawnElite：{}===",this.spawnElite);
            // 先等待死亡动画播放完，再在怪物列表对应位置中加入新怪
            // 可能要选择其它xy坐标成员变量
            AbstractMonster newMonster;
            if(!spawnElite){
                newMonster = new TouchOfSanguinarch(this.owner.hb_x + this.owner.hb_y, MathUtils.random(-5.0F, 25.0F));
            }
            else{
                newMonster = new GiftOfSanguinarch(this.owner.hb_x + this.owner.hb_y, MathUtils.random(-5.0F, 25.0F));
            }
            addToBot(new SpawnMonsterAction(newMonster, true));
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

