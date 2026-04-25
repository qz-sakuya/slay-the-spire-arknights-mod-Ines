package InesMod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

/**
 * 炮击提示 的特效触发器
 * 每隔固定时间生成一次 红色边框 特效
 */
public class FireTipEffect extends AbstractGameEffect {

    private float timer; // 计时器，控制特效生成频率
    private final float EFFECT_INTERVAL = 1.0f;

    public boolean stop = false;

    public FireTipEffect() {
        this.timer = EFFECT_INTERVAL; // 初始化计时器
    }

    @Override
    public void update() {
//        // 如果游戏禁用了特效，直接返回
//        if (com.megacrit.cardcrawl.core.Settings.DISABLE_EFFECTS) {
//            isDone = true;
//            return;
//        }

        // 更新计时器
        timer -= Gdx.graphics.getDeltaTime();



        // 当计时器小于等于0时，添加特效并重置计时器
        if (timer <= 0.0f && !stop) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderBreathingEffect(new Color(200, 0, 0,200))));
            timer = EFFECT_INTERVAL; // 重置为固定间隔
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        // 这个 Effect 本身不渲染任何东西，只是触发其他特效
    }

    @Override
    public void dispose() {
        // 无需释放资源
    }
}