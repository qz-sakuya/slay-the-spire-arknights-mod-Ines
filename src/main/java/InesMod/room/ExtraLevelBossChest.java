package InesMod.room;

import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.BlightHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.chests.BossChest;

import java.util.ArrayList;

public class ExtraLevelBossChest extends BossChest {
    public ExtraLevelBossChest() {
        this.img = ImageMaster.BOSS_CHEST;
        this.openedImg = ImageMaster.BOSS_CHEST_OPEN;
        this.hb = new Hitbox(256.0F * Settings.scale, 200.0F * Settings.scale);
        this.hb.move(CHEST_LOC_X, CHEST_LOC_Y - 100.0F * Settings.scale);
        if (AbstractDungeon.actNum >= 4 && AbstractPlayer.customMods.contains("Blight Chests")) { // 荒疫箱
            this.blights.clear();
            this.blights.add(BlightHelper.getRandomBlight());
            ArrayList<String> exclusion = new ArrayList();
            exclusion.add(this.blights.get(0).blightID);
            this.blights.add(BlightHelper.getRandomChestBlight(exclusion));
        } else {
            this.relics.clear();

            for(int i = 0; i < 3; ++i) {
                // 只放入罕见遗物
                this.relics.add(AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.UNCOMMON));
            }
        }
    }


}
