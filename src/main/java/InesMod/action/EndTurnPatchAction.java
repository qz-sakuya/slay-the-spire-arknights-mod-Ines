package InesMod.action;

import InesMod.helpers.LogHelper;
import InesMod.powers.AbstractInesPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

/**
 * EndTurnPatch 添加的动作
 */
public class EndTurnPatchAction extends AbstractGameAction {
    AbstractRoom room;

    public EndTurnPatchAction(AbstractRoom room) {
        this.room = room;
    }

    public void update() {
        LogHelper.info("===InesMod：EndTurnPatchAction：start, skipMonsterTurn={}===",room.skipMonsterTurn);
        if(room.skipMonsterTurn){

            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof AbstractInesPower
                        && ((AbstractInesPower) p).endOfRoundWhenSkipMonsterTurn){

                    p.atEndOfRound();
                }
            }

            for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
                for (AbstractPower p : mon.powers) {
                    if (p instanceof AbstractInesPower
                            && ((AbstractInesPower) p).endOfRoundWhenSkipMonsterTurn){

                        p.atEndOfRound();
                    }
                }
            }
        }




        this.isDone = true;
    }
}
