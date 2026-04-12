package InesMod.monsters.Chapter10;

import InesMod.action.ForceWaitAction;
import InesMod.helpers.PathHelper;
import InesMod.monsters.AbstractInesMonster;
import InesMod.powers.monster.LivingBlessingPower;
import InesMod.powers.monster.NewBranchesPower;
import InesMod.vfx.TeekazwurtzenLaserEffect;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

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
        super(ID, false, monsterStrings, EnemyType.NORMAL, 96, 150.0F, 180.0F, x, y);
        setImg(ID,"Teekazwurtzen.png");
        setWaitTime(0.0F);


        if (ascensionForHp()) {
            setHp(55);
        } else {
            setHp(50);
        }

        if (ascensionForDamage()) {
            this.attack = 12;
        } else {
            this.attack = 10;
        }

        this.damage.add(new DamageInfo(this, this.attack, DamageInfo.DamageType.NORMAL));
    }



    public void usePreBattleAction() {
        super.usePreBattleAction();

        addToBot(new ApplyPowerAction(this, this, new NewBranchesPower(this, -1), -1));
    }

    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
    }

    public void takeTurn() {
        setFastMode();
        switch (this.nextMove) {
            case 1:
                // 参考三柱的动画
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_FIRE", 0.5F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(new Color(200, 0, 0,200))));
                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new TeekazwurtzenLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.1F));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new TeekazwurtzenLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.3F));
                }
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE, Settings.FAST_MODE));
                break;
        }

        addToBot(new RollMoveAction(this));
    }

    public void changeState(String stateName) {
    }
}
