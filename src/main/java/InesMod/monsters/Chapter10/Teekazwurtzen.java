package InesMod.monsters.Chapter10;

import InesMod.action.ForceWaitAction;
import InesMod.helpers.PathHelper;
import InesMod.monsters.AbstractInesMonster;
import InesMod.powers.monster.LivingBlessingPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;

/**
 * 怪物中文名：提卡兹之根
 * 怪物英文名：Teekazwurtzen
 */
public class Teekazwurtzen extends AbstractInesMonster {
    public static final String ID = PathHelper.nameToId(Teekazwurtzen.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID); // 从游戏系统读取本地化资源

    int attack;
    int defend;


    public Teekazwurtzen(float x, float y) {
        super(ID, false, monsterStrings, EnemyType.NORMAL, 96, 240.0F, 230.0F, x, y);
        //setSpine(ID,"enemy_1345_tplamb", 1.6F);// TODO
        // setWaitTime(0.6F);
        this.state.setAnimation(0, "Idle", true);

        if (ascensionForHp()) {
            setHp(49);
        } else {
            setHp(42);
        }

        if (ascensionForDamage()) {
            this.attack = 21;
        } else {
            this.attack = 18;
        }

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.defend = 17;
        } else {
            this.defend = 15;
        }

        this.damage.add(new DamageInfo(this, this.attack, DamageInfo.DamageType.NORMAL));
    }



    public void usePreBattleAction() {
        super.usePreBattleAction();

        addToBot(new ApplyPowerAction(this, this, new LivingBlessingPower(this, -1,false), -1));
    }

    protected void getMove(int i) {
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
                addToBot(new ForceWaitAction(this.waitTime));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            case 2:
                addToBot(new GainBlockAction(this, this.defend));

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
