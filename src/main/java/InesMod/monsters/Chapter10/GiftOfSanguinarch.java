package InesMod.monsters.Chapter10;

import InesMod.action.ApplyNonStackPowerAction;
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
 * 怪物中文名：大君之赐
 * 怪物英文名：Gift of the Sanguinarch
 * （大造物）
 */
public class GiftOfSanguinarch extends AbstractInesMonster {
    public static final String ID = PathHelper.nameToId(GiftOfSanguinarch.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID); // 从游戏系统读取本地化资源


    int attack;
    int defend;

    float waitTime = 0.45F;

    public GiftOfSanguinarch(float x, float y) {
        super(ID, monsterStrings, EnemyType.NORMAL, 96, 240.0F, 230.0F, x, y);
        setSpine(ID,"enemy_1221_dzomg", 1.6F);


        setFastMode();
        this.state.setAnimation(0, "Idle", true);

        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(45);
        } else {
            setHp(40);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attack = 22;
        } else {
            this.attack = 18;
        }

        this.defend = 0;

        this.damage.add(new DamageInfo(this, this.attack, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.attack/2+1, DamageInfo.DamageType.NORMAL));
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
                addToBot(new WaitAction(this.waitTime));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
               // addToBot(new RemoveSpecificPowerAction(this, this, "Vigor"));
                break;
            case 2:
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new WaitAction(this.waitTime));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
               // addToBot(new RemoveSpecificPowerAction(this, this, "Vigor"));
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
