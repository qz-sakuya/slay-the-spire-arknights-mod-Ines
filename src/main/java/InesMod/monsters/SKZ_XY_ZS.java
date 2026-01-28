package InesMod.monsters;

import InesMod.cards.attack.BreakTheShadow;
import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class SKZ_XY_ZS extends AbstractInesMonster {
    public static final String ID = PathHelper.nameToId(SKZ_XY_ZS.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID); // 从游戏系统读取本地化资源

    private static final AbstractMonster.EnemyType TYPE = EnemyType.NORMAL;
    private static final String MID_PATH = "CPS/enemy_1345_tplamb"; // TODO
    private static final String ATTACK_NAME = "Attack";
    private static final String IDLE_NAME = "Idle";
    private static final String DIE_NAME = "Die";

    int attack;
    int defend;


    int white = 1;
    int strength = 2;
    float waitTime = 0.45F;

    public SKZ_XY_ZS(float x, float y) {
        super(monsterStrings.NAME, "eyjafjalla:CPS", TYPE, 96, 240.0F, 230.0F, x, y);
        setSpine("CPS/enemy_1345_tplamb", 1.6F);// TODO
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
        if ((i < 70 && !lastTwoMoves((byte)1)) || lastMove((byte)2)) {
            setMove((byte)1, AbstractMonster.Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        } else {
            setMove((byte)2, AbstractMonster.Intent.DEFEND_BUFF);
        }
    }

    public void takeTurn() {
        setFastMode();
        switch (this.nextMove) {
            case 1:
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new WaitAction(this.waitTime));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                //addToBot(new ApplyPowerAction((AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player, new WhiteCloudPower((AbstractCreature)AbstractDungeon.player, this.white), this.white));
                break;
            case 2:
                addToBot(new GainBlockAction(this, this.defend));
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, this.strength), this.strength));
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
