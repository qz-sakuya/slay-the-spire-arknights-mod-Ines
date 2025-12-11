package InesMod.patchs;

import InesMod.enums.InesCardTags;
import InesMod.helpers.PathHelper;
import InesMod.powers.AbstractInesPower;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.badlogic.gdx.graphics.Color;

/**
 *  给 renderPowerIcons 方法进行patch
 *  使其绘制第二个数字
 */
public class renderPowerSecondAmountPatch {
    public static final String ID = PathHelper.nameToId("CardTags");
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);


    @SpirePatch(clz = AbstractCreature.class, method = "renderPowerIcons")
    public static class Fun {
        @SpirePostfixPatch
        public static void Postfix(AbstractCreature __instance, SpriteBatch sb, float x, float y) {
            Color hbTextColor = ReflectionHacks.getPrivate(__instance, AbstractCreature.class, "hbTextColor");
            float POWER_ICON_PADDING_X = ReflectionHacks.getPrivate(__instance, AbstractCreature.class, "POWER_ICON_PADDING_X");

            float offset = 0.0F * Settings.scale;

            for (AbstractPower p : __instance.powers) {
                if (p instanceof AbstractInesPower){
                    if (Settings.isMobile) {
                        ((AbstractInesPower) p).renderSecondAmount(sb, x + offset + 32.0F * Settings.scale, y - 60.0F * Settings.scale, hbTextColor);
                    } else {
                        ((AbstractInesPower) p).renderSecondAmount(sb, x + offset + 32.0F * Settings.scale, y - 51.0F * Settings.scale, hbTextColor);
                    }
                    offset += POWER_ICON_PADDING_X;
                }
            }
        }


    }

    /*
     原版坐标：
     p.renderAmount(sb, x + offset + 32.0F * Settings.scale, y - 75.0F * Settings.scale, this.hbTextColor);
     p.renderAmount(sb, x + offset + 32.0F * Settings.scale, y - 66.0F * Settings.scale, this.hbTextColor);
     */


}
