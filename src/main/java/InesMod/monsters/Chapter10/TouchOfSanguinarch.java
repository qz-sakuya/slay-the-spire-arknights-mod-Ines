package InesMod.monsters.Chapter10;

import InesMod.action.ApplyNonStackPowerAction;
import InesMod.action.ForceWaitAction;
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
import com.megacrit.cardcrawl.monsters.AbstractMonster;

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


    public TouchOfSanguinarch(float x, float y) {
        super(ID, false, monsterStrings, EnemyType.NORMAL, 96, 240.0F, 135.0F, x, y);
        setSpine(ID,"enemy_1220_dzoms", 1.75F);
        setWaitTime(0.45F);
        this.state.setAnimation(0, "Idle", true);

        if (ascensionForHp()) {
            setHp(23);
        } else {
            setHp(20);
        }

        if (ascensionForDamage()) {
            this.attack = 11;
        } else {
            this.attack = 9;
        }

        this.defend = 0;

        this.damage.add(new DamageInfo(this, this.attack, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.attack/2+1, DamageInfo.DamageType.NORMAL));

    }



    public void usePreBattleAction() {
        super.usePreBattleAction();


        addToBot(new ApplyNonStackPowerAction(this, this, new RebornCreationPower(this, -1)));
    }

    protected void getMove(int i) {
        if (i < 70) {
            setMove((byte)1, AbstractMonster.Intent.ATTACK, this.damage.get(0).base);
        } else {
            // 二连击
            setMove((byte)2, AbstractMonster.Intent.ATTACK, this.damage.get(1).base, 2, true);
        }
    }

    public void takeTurn() {
        setFastMode();
        switch (this.nextMove) {
            case 1:
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new ForceWaitAction(this.waitTime));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
              //  addToBot(new RemoveSpecificPowerAction(this, this, "Vigor")); // 特判：清除活力
                break;
            case 2:
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new ForceWaitAction(this.waitTime));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            //    addToBot(new RemoveSpecificPowerAction(this, this, "Vigor"));
                break;
        }

        addToBot(new RollMoveAction(this));
    }

    public void changeState(String stateName) {
        switch (stateName) {
            case "START":
                this.state.setAnimation(0, "Start", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                break;
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
