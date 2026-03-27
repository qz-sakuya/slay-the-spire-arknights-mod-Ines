package InesMod.monsters.Chapter10;

import InesMod.helpers.PathHelper;
import InesMod.monsters.AbstractInesMonster;
import InesMod.powers.monster.DefenseArtilleryMeterPower;
import InesMod.powers.monster.LivingBlessingPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 怪物中文名：萨卡兹子裔工匠
 * 怪物英文名：Sarkaz Heirbearer Artificer
 */
public class SarkazHeirbearerArtificer extends AbstractInesMonster {
    public static final String ID = PathHelper.nameToId(SarkazHeirbearerArtificer.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID); // 从游戏系统读取本地化资源

    int attack;
    int defend;


    int white = 1;
    int strength = 2;
    float waitTime = 0.45F;

    public SarkazHeirbearerArtificer(float x, float y) {
        super(ID, monsterStrings, EnemyType.NORMAL, 96, 240.0F, 230.0F, x, y);
        setSpine(ID,"enemy_1223_dmech", 1.6F);
        setFastMode();
        this.state.setAnimation(0, "Idle", true);

        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(93);
        } else {
            setHp(78);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attack = 21;
        } else {
            this.attack = 18;
        }

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.defend = 21;
            this.white = 2;
        } else {
            this.defend = 18;
        }

        this.damage.add(new DamageInfo(this, this.attack, DamageInfo.DamageType.NORMAL));
    }

    public void setFastMode() {
        this.state.setTimeScale(1.0F);
        this.waitTime = 0.55F;

        if (Settings.FAST_MODE) {
            this.state.setTimeScale(2.0F);
            this.waitTime /= 2;
        }
    }

    public void usePreBattleAction() {
        super.usePreBattleAction();

        addToBot(new ApplyPowerAction(this, this, new DefenseArtilleryMeterPower(this, 0,4,40), 0));
        addToBot(new ApplyPowerAction(this, this, new LivingBlessingPower(this, -1,false), -1));
    }

    protected void getMove(int i) {
        // 开局立即充能
        // 每隔3回合充能1次
        // （必须有充能的价值，即所需充能不为2，因为生成意图的回合+下回合会累计2）
        // （如果所需充能为1，则本回合结束就会生成 炮击，下回合开始时，充能为0）
        if(moveHistory.isEmpty() ||
                (!checkHaveMoves(3,(byte)3) && getChargeRequired() != 2)){
            setMove((byte)3, Intent.BUFF);
            addToBot(new ChangeStateAction(this, "CHARGE_START"));
            return;
        }

        // 如果本回合会生成 炮击 ，则下回合起防
        if (AbstractDungeon.ascensionLevel >= 17 && getChargeRequired() <= 1) {
            setMove((byte)2, Intent.DEFEND);
            return;
        }

        if ((i < 70 && !lastTwoMoves((byte)1)) || lastMove((byte)2)) {
            setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
        } else {
            setMove((byte)2, Intent.DEFEND);
        }
    }

    public void takeTurn() {
        setFastMode();
        switch (this.nextMove) {
            case 1: // 攻击
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new WaitAction(this.waitTime));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
               break;
            case 2: // 防御
                addToBot(new GainBlockAction(this, this.defend));
              break;
            case 3: // 充能
                // 需要显示“城防炮充能......”
                AbstractPower powerToGet = this.getPower(DefenseArtilleryMeterPower.ID);
                if (powerToGet instanceof DefenseArtilleryMeterPower && getChargeRequired() > 0){
                    addToBot(new ApplyPowerAction(this, this, new DefenseArtilleryMeterPower(this, 1,((DefenseArtilleryMeterPower) powerToGet).secondAmount,((DefenseArtilleryMeterPower) powerToGet).damage), 1));
                }
                addToBot(new ChangeStateAction(this, "CHARGE_END"));
                break;
        }

        // roll下一个意图
        addToBot(new RollMoveAction(this));
    }

    public void changeState(String stateName) {
        switch (stateName) {
            case "ATTACK":
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                break;
            case "CHARGE_START":
                this.state.setAnimation(0, "Skill_Start", false);
                this.state.addAnimation(0, "Skill_Loop", true, 0.0F);
                break;
            case "CHARGE_END":
                this.state.setAnimation(0, "Skill_Loop", false);
                this.state.addAnimation(0, "Skill_End", false, 0.0F);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                break;
        }
    }

    public void die() {
        this.state.setTimeScale(1.0F);
        this.state.setAnimation(0, "Die", false);
        super.die();
    }

    // 计算还差多少充能就会生成 炮击
    private int getChargeRequired(){
        AbstractPower powerToGet = this.getPower(DefenseArtilleryMeterPower.ID);
        if (powerToGet instanceof DefenseArtilleryMeterPower) {
            return ((DefenseArtilleryMeterPower) powerToGet).secondAmount - powerToGet.amount;
        }
        return 999;
    }





    /*
    一种可能的行动预测（A17）

    回合   实际动作  下回合意图   城防炮充能        回合结束时     备注
                    充能	      0
    1        充能	          0->2
    2 		                  2->3
    3               防御       3->4            生成炮击
    4        防御    充能       0->1            炮击！
    5        充能              1->3
    6               防御       3->4            生成炮击
    7        防御              0->1            炮击！
    8               充能       1->2
    9        充能    防御       2->4            生成炮击
    10       防御               0->1            炮击！
    11                         1->2
    12                         2->3                    （没有充能价值）
    12              充能       3->4            生成炮击
    13      充能               0->2            炮击！
     */




}
