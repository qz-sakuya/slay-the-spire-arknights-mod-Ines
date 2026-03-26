package InesMod.monsters.Chapter10;

import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.monsters.AbstractInesMonster;
import InesMod.powers.monster.RebornCreationPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;

/**
 * 怪物中文名：大君之触
 * 怪物英文名：Touch of the Sanguinarch
 * （小造物）
 * 第一回合意图为？？？
 */
public class TouchOfSanguinarch extends AbstractInesMonster {
    public static final String ID = PathHelper.nameToId(TouchOfSanguinarch.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID); // 从游戏系统读取本地化资源

    int attack;
    int defend;

    float waitTime = 0.45F;

    public TouchOfSanguinarch(float x, float y) {
        super(ID, monsterStrings, EnemyType.NORMAL, 96, 240.0F, 230.0F, x, y);
        setSpine(ID,"enemy_1220_dzoms", 1.6F);


        setFastMode();
        this.state.setAnimation(0, "Idle", true);

        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(20);
        } else {
            setHp(24);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attack = 11;
        } else {
            this.attack = 9;
        }

        this.defend = 0;

        this.damage.add(new DamageInfo(this, this.attack, DamageInfo.DamageType.NORMAL));
    }

    public void setFastMode() {
        this.state.setTimeScale(1.0F);
        this.waitTime = 0.45F;

        if (Settings.FAST_MODE) {
            this.state.setTimeScale(2.0F);
            this.waitTime /= 2;
        }
    }

    public void usePreBattleAction() {
        super.usePreBattleAction();

        addToBot(new ApplyPowerAction(this, this, new RebornCreationPower(this, -1), -1));
    }

    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
    }

    public void takeTurn() {
        setFastMode();
        switch (this.nextMove) {
            case 1:
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new WaitAction(this.waitTime));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
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
