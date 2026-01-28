package InesMod.powers.test;

import InesMod.helpers.LogHelper;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 测试用power
 */
public class UNUSEDTestPower extends AbstractInesPower {
    public static final String ID = PathHelper.nameToId(UNUSEDTestPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID);

    public UNUSEDTestPower(AbstractCreature owner, int amount) {
        super(ID,
                true,
                powerStrings,
                owner,
                PowerType.BUFF,
                amount);
    }

    @Override
    public void atStartOfTurn() {
        LogHelper.info("===TestPower: atStartOfTurn, 当前owner:{}===",this.owner.name);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        LogHelper.info("===TestPower: atEndOfTurn, 当前owner:{}，isPlayer:{}===",this.owner.name,isPlayer);
    }

    @Override
    public void atEndOfRound( ) {
        LogHelper.info("===TestPower: atEndOfRound, 当前owner:{}===",this.owner.name);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        LogHelper.info("===TestPower: atDamageReceive, 受到伤害:{}===",damage);
        return damage;
    }

    @Override
    public void updateDescription() {

    }
}

