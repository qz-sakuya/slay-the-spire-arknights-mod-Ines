package InesMod.monsters.Chapter10;

import InesMod.action.ApplyNonStackPowerAction;
import InesMod.action.ForceWaitAction;
import InesMod.helpers.PathHelper;
import InesMod.monsters.AbstractInesMonster;
import InesMod.powers.monster.HatredPower;
import InesMod.powers.monster.LivingBlessingPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;

/**
 * 怪物中文名：萨卡兹子裔集恨者
 * 怪物英文名：Sarkaz Heirbearer Hatedrinker
 */
public class SarkazHeirbearerHatedrinker extends AbstractInesMonster {
    public static final String ID = PathHelper.nameToId(SarkazHeirbearerHatedrinker.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID); // 从游戏系统读取本地化资源

    int attack;

    boolean firstTimeBuff = true;

    public SarkazHeirbearerHatedrinker(float x, float y) {
        super(ID, monsterStrings, EnemyType.ELITE, 96, 240.0F, 325.0F, x, y);
        setSpine(ID,"enemy_1226_dklord", 1.55F);
        setWaitTime(1.14F);
        this.state.setAnimation(0, "Idle", true);

        if (ascensionForHp()) {
            setHp(145);
        } else {
            setHp(134);
        }

        if (ascensionForDamage()) {
            this.attack = 30;
        } else {
            this.attack = 25;
        }


        this.damage.add(new DamageInfo(this, this.attack, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, (int)(this.attack*1.5), DamageInfo.DamageType.NORMAL));

        this.firstTimeBuff = true;
    }



    public void usePreBattleAction() {
        super.usePreBattleAction();

        addToBot(new ApplyNonStackPowerAction(this, this, new HatredPower(this, 5)));
        addToBot(new ApplyNonStackPowerAction(this, this, new LivingBlessingPower(this, -1,true)));
    }

    protected void getMove(int i) {
        if(moveHistory.isEmpty() || !checkHaveMoves(3,(byte)3)){
            setMove((byte)3, Intent.BUFF);
            return;
        }

        if ((i < 70 && !lastTwoMoves((byte)1)) || lastMove((byte)2)) {
            setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
        } else {
            setMove((byte)2, Intent.ATTACK, this.damage.get(1).base);
        }
    }

    public void takeTurn() {
        setFastMode();
        switch (this.nextMove) {
            case 1: // 攻击
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new ForceWaitAction(this.waitTime));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
               break;
            case 2: // 重击
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new ForceWaitAction(this.waitTime));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
              break;
            case 3: // 强化
                // 清除所有debuff，获得再生
                addToBot(new RemoveDebuffsAction(this));

                int reborn = 2;
                if (firstTimeBuff){
                    firstTimeBuff = false;
                    reborn += 2;

                    if (ascensionForMove()){
                        reborn += 2;
                    }
                }

                addToBot(new ApplyPowerAction(this,this,new RegenerateMonsterPower(this,reborn),reborn));
                break;
        }

        addToBot(new RollMoveAction(this));
    }

    public void changeState(String stateName) {
        switch (stateName) {
            case "ATTACK":
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                break;
        }
    }

    public void die() {
        this.state.setTimeScale(1.0F);
        this.state.setAnimation(0, "Die", false);
        super.die();
    }
}
