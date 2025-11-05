package InesMod.action;

import InesMod.modcore.InesModMain;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

/**
 * 锉刀 的伤害效果
 * 判断目标是否有格挡
 */
public class FileAction extends AbstractGameAction {
    private DamageInfo info;
    private static final float DURATION = 0.01F;
    private static final float POST_ATTACK_WAIT_DUR = 0.1F;
    private AbstractMonster m;
    private int magicNumber;

    public FileAction(AbstractMonster target, DamageInfo info, int magicNumber) {
        this.info = info;
        setValues(target, info);
        this.m = target;
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.attackEffect = AbstractGameAction.AttackEffect.SLASH_VERTICAL;
        this.duration = 0.01F;
        this.magicNumber = magicNumber;
    }

    public void update() {
        if (this.target == null) {
            this.isDone = true;
            return;
        }

        // InesModMain.logger.info("===FileAction：原伤害{}===", this.info.output);
        // InesModMain.logger.info("===FileAction：目标格挡{}===", this.m.currentBlock);
        if (this.m.currentBlock > 0) { // 有格挡
            this.info.output *= this.magicNumber;
            this.info.isModified = true;
        }
        // InesModMain.logger.info("===FileAction：判断后伤害{}===", this.info.output);

        if (this.duration == 0.01F && this.target != null && this.target.currentHealth > 0) {
            if (this.info.type != DamageInfo.DamageType.THORNS &&
                    this.info.owner.isDying) {
                this.isDone = true;
                return;
            }
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
        }

        tickDuration();

        if (this.isDone && this.target != null && this.target.currentHealth > 0) {
            this.target.damage(this.info);
            if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
            addToTop(new WaitAction(0.1F));
        }

        this.isDone = true;
    }
}
