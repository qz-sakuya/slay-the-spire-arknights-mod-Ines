package InesMod.monsters;


import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractInesMonster extends AbstractMonster {
    public AbstractInesMonster(String name, String id, AbstractMonster.EnemyType type, int health, float hb_width, float hb_height, float x, float y) {
        super(name, id, health, 0.0F, 0.0F, hb_width, hb_height, null, x, y);
        this.type = type;
    }

    public void setSpine(String midPath, float divScale) {
        loadAnimation("InesModResources/img/monsters/" + midPath + ".atlas", "InesModResources/img/monsters/" + midPath + ".json", divScale);
        this.flipHorizontal = true;
    }
}