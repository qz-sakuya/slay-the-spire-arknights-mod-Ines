package InesMod.monsters.Chapter10;

import InesMod.action.ApplyNonStackPowerAction;
import InesMod.action.ForceWaitAction;
import InesMod.action.SetPowerAction;
import InesMod.action.SummonWarriorAction;
import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.monsters.AbstractInesMonster;
import InesMod.patchs.ExtraLevelPatch;
import InesMod.powers.AbstractInesPower;
import InesMod.powers.monster.*;
import InesMod.powers.player.NoInvisibilityPower;
import InesMod.powers.player.StrengthStealPower;
import InesMod.powers.player.StrengthStolenPower;
import InesMod.relics.AbstractInesRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.BronzeOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * 怪物中文名：曼弗雷德
 * 怪物英文名：Manfred
 *
 *
 *
 *
 *
 * 2轻1重为1组，顺序随机，但一定都会打。都是单击。
 * 重后面必定跟1防御
 *
 * 2阶段防御替换为召唤，如果已经召唤，防御
 * 2阶段所有攻击的连击+1，防御提升
 *
 * 记得通知怪物死亡回调
 *
 */
public class Manfred extends AbstractInesMonster {
    public static final String ID = PathHelper.nameToId(Manfred.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID); // 从游戏系统读取本地化资源

    public static final float[] WARRIOR_POSX = new float[] { -400.0F, -150.0F};
    public static final float[] WARRIOR_POSY = new float[] { 0.0F, 0.0F };


    int attack;
    int defend;

    private ArrayList<Byte> attackMoveList;
    private boolean hasCallWarrior = false;


    public Manfred(float x, float y) {
        super(ID, true, monsterStrings, EnemyType.BOSS, 96, 300.0F, 320.0F, x, y);
        setSpine(ID,"enemy_1528_manfri", 1.45F);

        setWaitTime(0.6F);
        this.state.setAnimation(0, "Idle", true);


        if (ascensionForHp()) {
            setHp(420);
        } else {
            setHp(400);
        }

        if (ascensionForDamage()) {
            this.attack = 23;
        } else {
            this.attack = 20;
        }

        if (ascensionForMove()) {
            this.defend = 20;
        } else {
            this.defend = 17;
        }


        this.damage.add(new DamageInfo(this, this.attack, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, (int)(this.attack*0.75), DamageInfo.DamageType.NORMAL));


        // 初始化攻击列表
        attackMoveList = new ArrayList<>(Arrays.asList((byte)1, (byte)1, (byte)2));
    }



    public void usePreBattleAction() {
        AbstractDungeon.scene.fadeOutAmbiance();
        CardCrawlGame.music.unsilenceBGM();
        ExtraLevelPatch.BOSS_MUSIC_KEY = "C10_Boss_1";
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");

        AbstractDungeon.getCurrRoom().cannotLose = true; // 其中一个作用是标记1、2阶段


        addToBot(new ApplyPowerAction(this, this, new DefenseArtilleryMeterPower(this, 0,4,40), 0));
        addToBot(new ApplyPowerAction(this, this, new ManfredFocusPower(this, 2), 2));
        addToBot(new ApplyPowerAction(this, this, new UnyieldingPower(this, -1), -1));

        if (ascensionForMove()){
            addToBot(new ApplyPowerAction(this, this, new MilitaryTrainingPower(this, -1,1), -1));
        }
        else {
            addToBot(new ApplyPowerAction(this, this, new MilitaryTrainingPower(this, -1,2), -1));
        }

        super.usePreBattleAction();
    }

    protected void getMove(int i) {
        if(!hasCallWarrior && !AbstractDungeon.getCurrRoom().cannotLose) { // 二阶段
            // 如果怪物列表前2位都死了，召唤两个战士
            boolean haveAliveMonster = false;

            ArrayList<AbstractMonster> monsterList = AbstractDungeon.getMonsters().monsters;
            int checkCount = Math.min(2, monsterList.size());

            for (int index = 0; index < checkCount; index++) {
                AbstractMonster mon = monsterList.get(index);
                if (mon == this){
                    continue;
                }

                if (mon != null && !mon.halfDead && !mon.isDying && !mon.isDead) {
                    haveAliveMonster = true;
                    break;
                }
            }

            if (!haveAliveMonster) {
                setMove((byte) 6, AbstractMonster.Intent.UNKNOWN);
                hasCallWarrior = true;
                return;
            }
        }


       if (!attackMoveList.isEmpty()){
           int size = attackMoveList.size();
           int index = i % size;
           byte move = attackMoveList.remove(index);

           if (move == 1) { // 轻击
               if(moveHistory.isEmpty() || AbstractDungeon.getCurrRoom().cannotLose){ // 一阶段
                   setMove((byte)1, AbstractMonster.Intent.ATTACK, this.damage.get(0).base);
               }
               else{
                   setMove((byte)3, AbstractMonster.Intent.ATTACK, this.damage.get(0).base,2, true); // 二连击
               }
           } else { // 重击
               if(moveHistory.isEmpty() || AbstractDungeon.getCurrRoom().cannotLose){ // 一阶段
                   setMove((byte)2, AbstractMonster.Intent.ATTACK, this.damage.get(1).base,2, true); // 二连击
               }
               else{
                   setMove((byte)4, AbstractMonster.Intent.ATTACK, this.damage.get(0).base,3, true); // 三连击
               }
           }
       }
       else { // 攻击列表为空
           attackMoveList = new ArrayList<>(Arrays.asList((byte)1, (byte)1, (byte)2)); // 补满

           if(!AbstractDungeon.getCurrRoom().cannotLose) { // 二阶段
               // 如果没有提卡兹之根，召唤
               boolean haveTeekazwurtzen = false;
               for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
                   if (mon.halfDead || mon.isDying || mon.isDead) {
                       continue;
                   }
                   if (mon instanceof Teekazwurtzen) {
                       haveTeekazwurtzen = true;
                       break;
                   }
               }
               if (!haveTeekazwurtzen) {
                   setMove((byte)7, AbstractMonster.Intent.UNKNOWN);
                   return;
               }
           }

           // 防御
           setMove((byte)5, Intent.DEFEND);
       }


    }

    public void takeTurn() {
        LogHelper.info("===曼弗雷德：takeTurn，nextMove={}===",this.nextMove);
        setFastMode();
        switch (this.nextMove) {
            case 1: // 一阶段轻击（倍率100%）
                addToBot(new ChangeStateAction(this, "ATTACK_1"));
                addToBot(new ForceWaitAction(this.waitTime));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                break;
            case 2: // 一阶段重击（倍率150%）
                addToBot(new ChangeStateAction(this, "ATTACK_1"));
                addToBot(new ForceWaitAction(this.waitTime));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                break;
            case 3: // 二阶段轻击（倍率200%）
                addToBot(new ChangeStateAction(this, "ATTACK_2"));
                addToBot(new ForceWaitAction(this.waitTime));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
            case 4: // 二阶段重击（倍率300%）
                addToBot(new ChangeStateAction(this, "ATTACK_2"));
                addToBot(new ForceWaitAction(this.waitTime));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            case 5: // 防御
                addToBot(new GainBlockAction(this, this.defend));
                break;
            case 6: // 2阶段召唤战士
                addToBot(new ChangeStateAction(this, "ATTACK_2"));

                if (MathUtils.random(100) > 50) {
                    int randomIndex = MathUtils.random(1);
                    addToBot(new TalkAction(this,monsterStrings.DIALOG[randomIndex]));
                }

                addToBot(new SummonWarriorAction(0, WARRIOR_POSX[0], WARRIOR_POSY[0]));
                addToBot(new SummonWarriorAction(1, WARRIOR_POSX[1], WARRIOR_POSY[1]));
                break;
            case 7: // 2阶段召唤提卡兹之根
                int currentSize = AbstractDungeon.getCurrRoom().monsters.monsters.size();
                AbstractMonster teekazwurtzen = new Teekazwurtzen(0, 0);

                // 修正位置
                teekazwurtzen.drawX = this.drawX + 320.0F;
                teekazwurtzen.drawY = this.drawY + 150.0F;

                teekazwurtzen.usePreBattleAction();
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(teekazwurtzen, false, currentSize));
                break;
            case 8: // 重生
                setFastModeTo(false);
                addToBot(new ChangeStateAction(this,"REBIRTH"));

                this.halfDead = false;

                addToBot(new HealAction(this,this,maxHealth));

                // 强化自身
                this.defend = (int)(this.defend *1.5);

                AbstractPower tmpPower = this.getPower(MilitaryTrainingPower.ID);
                if (tmpPower instanceof MilitaryTrainingPower) {
                    MilitaryTrainingPower militaryTrainingPower = (MilitaryTrainingPower) tmpPower;
                    militaryTrainingPower.setReductionRatio(0.8F,"80%");
                    militaryTrainingPower.setToInvalidTurn(1);
                    militaryTrainingPower.clearInvalidTurn();
                }



                // 清空城防炮充能
                // 如果有刚施加的炮击，不清空
                boolean hasFirePower = false;
                AbstractPower powerToGet = AbstractDungeon.player.getPower(FirePower.ID);
                if (powerToGet != null) {
                    hasFirePower = true;
                }
                if (!hasFirePower) {
                    AbstractPower tempPower = this.getPower(DefenseArtilleryMeterPower.ID);
                    if (tempPower instanceof DefenseArtilleryMeterPower) {
                        DefenseArtilleryMeterPower damPower = (DefenseArtilleryMeterPower) tempPower;
                        addToBot(new SetPowerAction(this, this, new DefenseArtilleryMeterPower(this, 0, damPower.secondAmount, damPower.damage), 0));
                        damPower.notAddThisTurn = true;
                    }
                }


                addToBot(new CanLoseAction());

                CardCrawlGame.music.fadeOutTempBGM();
                ExtraLevelPatch.BOSS_MUSIC_KEY = "C10_Boss_2";
                AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");

                if (MathUtils.random(100) > 50) {
                    int randomIndex = MathUtils.random(1);
                    addToBot(new TalkAction(this, monsterStrings.DIALOG[2 + randomIndex]));
                }

                attackMoveList = new ArrayList<>(Arrays.asList((byte) 1, (byte) 2)); // 复合后进行两次攻击

                break;
        }

        addToBot(new RollMoveAction(this));
    }

    public void changeState(String stateName) {
        switch (stateName) {
            case "ATTACK_1":
                this.state.setAnimation(0, "Attack_1", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                break;

            case "ATTACK_2":
                this.state.setAnimation(0, "Attack_2", false);
                this.state.addAnimation(0, "Idle_2", true, 0.0F);
                break;

            case "REBIRTH":
                this.state.addAnimation(0, "Revive", false, 0.0F);
                this.state.addAnimation(0, "Idle_2", true, 0.0F);
                break;
        }

    }






    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead) {
            LogHelper.info("===曼弗雷德：damage：死亡===");

            if (AbstractDungeon.getCurrRoom().cannotLose) {
                this.halfDead = true;

                setFastModeTo(false);
                this.state.setAnimation(0, "Die", false);
                // this.state.addAnimation(0, "Die_Idle", true, 0.0F);

                // 充满城防炮充能并施加炮击
                AbstractPower tempPower = this.getPower(DefenseArtilleryMeterPower.ID);
                if (tempPower instanceof DefenseArtilleryMeterPower) {
                    DefenseArtilleryMeterPower damPower = (DefenseArtilleryMeterPower) tempPower;
                    addToBot(new SetPowerAction(this, this, new DefenseArtilleryMeterPower(this, damPower.secondAmount,damPower.secondAmount,damPower.damage), damPower.secondAmount));

                    // 给玩家添加power
                    addToBot(new ApplyNonStackPowerAction(AbstractDungeon.player, this, new FirePower(AbstractDungeon.player, -1, damPower.damage)));

                    // 给所有怪物添加power
                    for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
                        if (mon instanceof Teekazwurtzen){
                            continue; // 跳过提卡兹之根
                        }

                        if (mon == this){
                            continue; // 跳过自己
                        }

                        addToBot(new ApplyNonStackPowerAction(mon, this, new FirePower(mon, -1, damPower.damage)));
                    }
                }
            }

            for(AbstractPower p : this.powers) {
                p.onDeath();
            }

            for(AbstractRelic r : AbstractDungeon.player.relics) {
                r.onMonsterDeath(this);
            }

            // 触发自定义回调
            for (AbstractPower power : AbstractDungeon.player.powers) {
                if (power instanceof AbstractInesPower) {
                    ((AbstractInesPower) power).onAnyMonsterDeath(this);
                }
            }

            for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
                for (AbstractPower power : mon.powers) {
                    if (power instanceof AbstractInesPower) {
                        ((AbstractInesPower) power).onAnyMonsterDeath(this);
                    }
                }
            }
            // 触发自定义回调 end


            this.addToTop(new ClearCardQueueAction());

            // 清空 debuff +所有力量相关+炮击+重生提示
            Iterator<AbstractPower> s = this.powers.iterator();
            while(s.hasNext()) {
                AbstractPower p = (AbstractPower)s.next();
                if (p.type == AbstractPower.PowerType.DEBUFF
                        || p.ID.equals(StrengthPower.POWER_ID)
                        || p.ID.equals(StrengthStealPower.ID)
                        || p.ID.equals(StrengthStolenPower.ID)
                        || p.ID.equals("Shackled")
                        || p.ID.equals(FirePower.ID)
                        || p.ID.equals(UnyieldingPower.ID)) {
                    s.remove();
                }
            }

            this.setMove((byte)8, Intent.UNKNOWN);
            this.createIntent();
            this.addToBot(new SetMoveAction(this, (byte)8, Intent.UNKNOWN));

            this.applyPowers();
        }
    }




    @Override
    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            this.useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            (AbstractDungeon.getCurrRoom()).rewardAllowed = false;

            for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                if (!m.isDying) {
                    if (m instanceof Teekazwurtzen){
                        addToBot(new SuicideAction(m));
                    }
                    else{
                        addToBot(new EscapeAction(m));
                    }
                }
            }


            this.state.setTimeScale(1.0F);
            this.state.setAnimation(0, "Die_2", false);
            this.onBossVictoryLogic();
            CardCrawlGame.stopClock = true;
            super.die();
        }
    }
}
