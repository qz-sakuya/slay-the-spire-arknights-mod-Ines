package InesMod.monsters.Chapter10;

import InesMod.helpers.PathHelper;
import InesMod.monsters.AbstractInesMonster;
import InesMod.powers.monster.DefenseArtilleryMeterPower;
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
            setHp(84);
        } else {
            setHp(72);
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
    }

    protected void getMove(int i) {
        if(moveHistory.isEmpty() || checkSpecificMove(4,(byte)3)){
            setMove((byte)3, Intent.BUFF);
        }

        // 炮击前起防
        AbstractPower powerToGet = this.getPower(DefenseArtilleryMeterPower.ID);
        if (AbstractDungeon.ascensionLevel >= 17 && powerToGet instanceof DefenseArtilleryMeterPower && powerToGet.amount == 3) {
            setMove((byte)2, Intent.DEFEND);
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
            case 1:
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new WaitAction(this.waitTime));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
               break;
            case 2:
                addToBot(new GainBlockAction(this, this.defend));
              break;
            case 3:
                // 需要显示“城防炮充能......”
                AbstractPower powerToGet = this.getPower(DefenseArtilleryMeterPower.ID);
                if (powerToGet instanceof DefenseArtilleryMeterPower && powerToGet.amount < ((DefenseArtilleryMeterPower) powerToGet).secondAmount) {
                    addToBot(new ApplyPowerAction(this, this, new DefenseArtilleryMeterPower(this, 1,((DefenseArtilleryMeterPower) powerToGet).secondAmount,((DefenseArtilleryMeterPower) powerToGet).damage), 1));
                }
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
