package InesMod.action;

import InesMod.vfx.GuidedThrowEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * 牵引投掷 的效果
 */
public class GuidedThrowAction extends AbstractGameAction {
    AbstractPlayer player;
    int[] multiDamage = new int[0]; // 每个敌人的基础伤害数组

    public GuidedThrowAction(AbstractPlayer player, int[] multiDamage, DamageInfo.DamageType type) {
        this.player = player;
        this.multiDamage = multiDamage;
        this.damageType = type;
    }

    public void update() {
        tickDuration();
        if (this.isDone) {
            for (AbstractPower power : player.powers) {
                power.onDamageAllEnemies(this.multiDamage);
            }
            Iterator<AbstractMonster> var1 = (AbstractDungeon.getMonsters()).monsters.iterator();
            ArrayList<AbstractGameAction> actions = new ArrayList<>();
            int index = 0;
            if (!(AbstractDungeon.getMonsters()).monsters.isEmpty()) {
                AbstractCreature prev = player; // 攻击动画从玩家开始
                while (var1.hasNext()) {
                    AbstractMonster curMonster = var1.next();
                    if (!curMonster.isDeadOrEscaped()) {
                        actions.add(new VFXAction(new GuidedThrowEffect(prev.hb.cX, prev.hb.cY, curMonster.hb.cX, curMonster.hb.cY), 0.4F));
                        actions.add(new WaitAction(0.1F));
                        actions.add(new DamageAction(curMonster, new DamageInfo(this.player, this.multiDamage[index], this.damageType), AttackEffect.SLASH_VERTICAL));

                        // actions.add(new GuidedThrowAttackAction(player,prev,curMonster,multiDamage[index],damageType,null));

                        prev = curMonster;
                        index++;
                    }
                }
            }
            Collections.reverse(actions);
            for (AbstractGameAction action : actions) addToTop(action);
        }
    }
}
