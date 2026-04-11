package InesMod.monsters.Chapter10;

import InesMod.action.ApplyNonStackPowerAction;
import InesMod.action.ForceWaitAction;
import InesMod.helpers.PathHelper;
import InesMod.monsters.AbstractInesMonster;
import InesMod.powers.monster.BouncePower;
import InesMod.powers.monster.LivingBlessingPower;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;

/**
 * 怪物中文名：萨卡兹子裔链术师
 * 怪物英文名：Sarkaz Heirbearer Chain Caster
 */
public class SarkazHeirbearerChainCaster extends AbstractInesMonster {
    public static final String ID = PathHelper.nameToId(SarkazHeirbearerChainCaster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID); // 从游戏系统读取本地化资源

    int attack;


    public SarkazHeirbearerChainCaster(float x, float y) {
        super(ID, false, monsterStrings, EnemyType.ELITE, 96, 240.0F, 325.0F, x, y);
        setSpine(ID,"enemy_1225_dkmage", 1.55F);
        setWaitTime(1.15F);
        this.state.setAnimation(0, "Idle", true);

        if (ascensionForHp()) {
            setHp(118);
        } else {
            setHp(108);
        }

        if (ascensionForDamage()) {
            this.attack = 18;
        } else {
            this.attack = 15;
        }


        this.damage.add(new DamageInfo(this, this.attack, DamageInfo.DamageType.NORMAL));
    }



    public void usePreBattleAction() {
        super.usePreBattleAction();

        addToBot(new ApplyNonStackPowerAction(this, this, new BouncePower(this, -1)));
        addToBot(new ApplyNonStackPowerAction(this, this, new LivingBlessingPower(this, -1,true)));
    }

    protected void getMove(int i) {
        if ((i < 70 && !lastTwoMoves((byte)1)) || lastMove((byte)2)) {
            setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
        } else {
            setMove((byte)2, Intent.DEBUFF);
        }
    }

    public void takeTurn() {
        setFastMode();
        switch (this.nextMove) {
            case 1:
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new ForceWaitAction(this.waitTime));

                if (MathUtils.randomBoolean()) {
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("GHOST_ORB_IGNITE_1", 0.3F));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("GHOST_ORB_IGNITE_2", 0.3F));
                }
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case 2:
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new ForceWaitAction(this.waitTime));
                // 给予1回合脆弱
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new FrailPower(AbstractDungeon.player,1,true),1));
                // 进阶：额外给予1回合虚弱
                if(ascensionForMove()){
                    addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new WeakPower(AbstractDungeon.player,1,true),1));
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
