package InesMod.monsters;

import InesMod.helpers.PathHelper;
import InesMod.powers.monster.CFPPower;
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
 * 怪物中文名：萨卡兹城防炮手
 */
public class SKZ_CFPS extends AbstractInesMonster {
    public static final String ID = PathHelper.nameToId(SKZ_CFPS.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID); // 从游戏系统读取本地化资源

    int attack;
    int defend;


    int white = 1;
    int strength = 2;
    float waitTime = 0.45F;

    public SKZ_CFPS(float x, float y) {
        super(ID, monsterStrings, EnemyType.NORMAL, 96, 240.0F, 230.0F, x, y);
        //setSpine(ID,"enemy_1345_tplamb", 1.6F);// TODO
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
        if (Settings.FAST_MODE) {
            this.state.setTimeScale(2.0F);
            this.waitTime = 0.225F;
        } else {
            this.state.setTimeScale(1.0F);
            this.waitTime = 0.45F;
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
        AbstractPower powerToGet = this.getPower(CFPPower.ID);
        if (AbstractDungeon.ascensionLevel >= 17 && powerToGet instanceof CFPPower && powerToGet.amount == 3) {
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
                AbstractPower powerToGet = this.getPower(CFPPower.ID);
                if (powerToGet instanceof CFPPower && powerToGet.amount < ((CFPPower) powerToGet).secondAmount) {
                    addToBot(new ApplyPowerAction(this, this, new CFPPower(this, 1,((CFPPower) powerToGet).secondAmount,((CFPPower) powerToGet).damage), 1));
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
