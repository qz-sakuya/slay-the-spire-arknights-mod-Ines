package InesMod.powers;

import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 中文名：炮击！
 * 敌方power
 */
public class FirePower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(FirePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public FirePower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount); // 一般为40伤
    }


    @Override
    public void atStartOfTurn() { // can only monster?
        // 哪怕怪不是第一个位置，也应该在回合开始，攻击前先触发

        // 对所有敌方造成伤害（固定伤害，参考爆炸机）
        ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
        int[] tmp = new int[m.size()];
        Arrays.fill(tmp, this.amount);
        addToBot(new DamageAllEnemiesAction(this.owner, tmp, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));

        // 对玩家造成伤害
        addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE));


        addToBot(new ReducePowerAction(owner, owner, FirePower.ID, amount));
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.amount);
    }
}
