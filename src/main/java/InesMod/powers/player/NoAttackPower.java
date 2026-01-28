package InesMod.powers.player;

import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：不能攻击
 * 此 power 的效果与原版 EntanglePower 的相同
 * 只是为了换个power图标
 */
public class NoAttackPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(NoAttackPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public NoAttackPower(AbstractCreature owner, int amount) {
        super(ID,
                false,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount); // 此power不可叠加
    }


    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0], this.amount);
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        if (AbstractDungeon.player.hasPower("Entangled")){
            return true; // 不重复触发对话框
        }

        if (card.type == AbstractCard.CardType.ATTACK) {
            return false;
        }

        return true;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer)
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, NoAttackPower.ID));
    }


}
