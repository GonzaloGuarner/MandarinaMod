package mandarinamod.vfx.combat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import mandarinamod.util.SoundManager;

public class BlockMassRemovalEffect extends AbstractGameEffect {
    private final float x;
    private final float y;
    private boolean triggered = false;

    private static final String SOUND_ID = SoundManager.STRONG_WIND;

    public BlockMassRemovalEffect(float x, float y) {
        this.x = x;
        this.y = y;
        this.duration = Settings.FAST_MODE ? 0.5F : 1F;
        this.startingDuration = this.duration;
    }

    @Override
    public void update() {
        if (!triggered) {
            triggered = true;

            CardCrawlGame.sound.play(SOUND_ID);

            // flash border and emit shockwave
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.TEAL.cpy(), true));
            AbstractDungeon.effectsQueue.add(new ShockwaveEffect(this.x, this.y, Color.ROYAL.cpy(), duration));
        }
        this.isDone = true;
//        this.duration -= Gdx.graphics.getDeltaTime();
//        if (this.duration <= startingDuration/2F) {
//
//        }
    }

    @Override
    public void render(SpriteBatch sb) { }
    @Override
    public void dispose() { }

    private static class ShockwaveEffect extends AbstractGameEffect {
        private final float x;
        private final float y;
        private final Color color;
        private final float fadeDuration;

        public ShockwaveEffect(float x, float y, Color color, float duration) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.duration = duration;
            this.startingDuration = duration;
            this.fadeDuration = duration / 2f;
        }

        @Override
        public void update() {
            this.duration -= Gdx.graphics.getDeltaTime();
            if (this.duration < this.fadeDuration) {
                this.color.a = this.duration / this.fadeDuration;
            }
            if (this.duration <= 0F) this.isDone = true;
        }

        @Override
        public void render(SpriteBatch sb) {
            sb.setColor(this.color);
            sb.draw(com.megacrit.cardcrawl.helpers.ImageMaster.WHITE_RING.getTexture(),
                    this.x - 256f, this.y - 256f,
                    256f, 256f,
                    512f, 512f,
                    Settings.scale, Settings.scale, 0,
                    com.megacrit.cardcrawl.helpers.ImageMaster.WHITE_RING.getRegionX(),
                    com.megacrit.cardcrawl.helpers.ImageMaster.WHITE_RING.getRegionY(),
                    com.megacrit.cardcrawl.helpers.ImageMaster.WHITE_RING.getRegionWidth(),
                    com.megacrit.cardcrawl.helpers.ImageMaster.WHITE_RING.getRegionHeight(),
                    false, false);
        }

        @Override
        public void dispose() { }
    }
}
