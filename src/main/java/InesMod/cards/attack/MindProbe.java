package InesMod.cards.attack;

import InesMod.action.MindProbeAction;
import InesMod.cards.AbstractInesCard;
import InesMod.characters.Ines;
import InesMod.helpers.PathHelper;
import InesMod.vfx.InesAttackEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 中文卡名：心灵探查
 */
public class MindProbe extends AbstractInesCard {
    public static final String ID = PathHelper.nameToId(MindProbe.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源

    int updateAmount;

    public MindProbe() {
        super(ID,
                false,
                cardStrings,
                1,
                CardType.ATTACK,
                CardRarity.COMMON,
                CardTarget.ALL_ENEMY,
                Ines.Enums.INES_CARD);
        this.baseDamage = 6;
        this.isMultiDamage = true;

        this.updateAmount = 1;
        this.magicNumber = this.baseMagicNumber = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot(new MindProbeAction(p, 0, updateAmount));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        Iterator<AbstractMonster> var1 = (AbstractDungeon.getMonsters()).monsters.iterator();
        ArrayList<AbstractMonster.Intent> seenIntent = new ArrayList<>();

        if (!(AbstractDungeon.getMonsters()).monsters.isEmpty()) {
            while (var1.hasNext()) {
                AbstractMonster curMonster = var1.next();
                if (!curMonster.isDeadOrEscaped() && !seenIntent.contains(curMonster.intent)) {
                    seenIntent.add(curMonster.intent);
                }
            }
        }
        this.baseMagicNumber = seenIntent.size() * updateAmount;;

        // 添加额外文本
        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);

        }
    }
}
