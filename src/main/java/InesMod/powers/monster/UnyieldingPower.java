package InesMod.powers.monster;

import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 中文名：不屈
 * 敌方power
 * 仅提示boss可以重生
 */
public class UnyieldingPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(UnyieldingPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID); // 从游戏系统读取本地化资源

    public UnyieldingPower(AbstractCreature owner, int amount) {
        super(ID,
                true,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount); // 不可叠加

        this.loadRegion("unawakened");
    }

    @Override
    public void updateDescription() {
        this.description = String.format(descriptions[0]);
    }
}

