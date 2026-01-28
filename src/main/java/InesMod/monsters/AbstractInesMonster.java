package InesMod.monsters;


import InesMod.helpers.PathHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractInesMonster extends AbstractMonster {
    public AbstractInesMonster(String ID,
                               MonsterStrings strings,
                               AbstractMonster.EnemyType type,
                               int health,
                               float hb_width,
                               float hb_height,
                               float x,
                               float y) {
        super(strings.NAME, ID, health, 0.0F, 0.0F, hb_width, hb_height, null, x, y);
        this.type = type;
    }

    public void setSpine(String ID, String fileName, float divScale) {
        String monsterName = PathHelper.idToName(id);
        loadAnimation("InesModResources/img/monsters/" + monsterName + '/' + fileName + ".atlas", "InesModResources/img/monsters/" + monsterName + '/' + fileName + ".json", divScale);
        this.flipHorizontal = true;
    }
}