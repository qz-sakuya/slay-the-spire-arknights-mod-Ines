package InesMod.monsters.Chapter10;

import InesMod.action.ApplyNonStackPowerAction;
import InesMod.helpers.PathHelper;
import InesMod.monsters.AbstractInesMonster;
import InesMod.powers.monster.SanguineInfusionPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 怪物中文名：萨卡兹子裔补给车
 * 怪物英文名：Sarkaz Heirbearer ASV
 */
public class SarkazHeirbearerASV extends AbstractInesMonster {
    public static final String ID = PathHelper.nameToId(SarkazHeirbearerASV.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID); // 从游戏系统读取本地化资源

    int attack;
    int defend;


    public SarkazHeirbearerASV(float x, float y) {
        super(ID, monsterStrings, EnemyType.NORMAL, 96, 240.0F, 230.0F, x, y);
        setSpine(ID,"enemy_1224_dsuply", 1.6F);
        setWaitTime(0.45F);
        this.state.setAnimation(0, "Idle", true);

        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(104);
        } else {
            setHp(95);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attack = 14;
        } else {
            this.attack = 11;
        }

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.defend = 20;
        } else {
            this.defend = 17;
        }

        this.damage.add(new DamageInfo(this, this.attack, DamageInfo.DamageType.NORMAL));
    }



    public void usePreBattleAction() {
        super.usePreBattleAction();

        addToBot(new ApplyPowerAction(this, this, new SanguineInfusionPower(this, 6),6));
    }

    // 40概率防御 20概率攻击 40概率强化
    // 第1、2回合不会强化
    protected void getMove(int i) {
        // 第三回合必定强化
        if (AbstractDungeon.ascensionLevel >= 17 && moveHistory.size() == 2){
            setMove((byte)3, Intent.BUFF);
            return;
        }

        if (moveHistory.size() >= 2 && i >= 60){
            setMove((byte)3, Intent.BUFF);
            return;
        }

        if (i < 40 || lastMove((byte)2)) {
            setMove((byte)1, Intent.DEFEND);
        }
        else {
            setMove((byte)2, Intent.ATTACK, this.damage.get(0).base);
        }
    }

    public void takeTurn() {
        setFastMode();
        switch (this.nextMove) {
            case 1: // 防御
                addToBot(new GainBlockAction(this, this.defend));
                break;
            case 2: // 攻击
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new WaitAction(this.waitTime));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            case 3: // 强化
//                int tmpDefend = (int) (this.defend * 0.8);
//                addToBot(new GainBlockAction(this, tmpDefend));
                addToBot(new ApplyPowerAction(this, this, new SanguineInfusionPower(this, 3),3));
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
