package mandarinamod.vfx.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import static mandarinamod.MandarinaMod.makeID;

public class AuroraRayEffect extends AbstractGameEffect {
    private final float x;
    private final float y;

    public AuroraRayEffect(float x, float y) {
        this.x = x;
        this.y = y;
        this.duration = 1.5F;
        this.startingDuration = 1.5F;
    }

    @Override
    public void update() {
        if (this.duration == this.startingDuration) {

            CardCrawlGame.sound.play(makeID("AURORA"));

            // Add initial border flash with aurora colors
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.TEAL.cpy(), true));

            // Add aurora gradient lightning
            for (int i = 0; i < 12; i++) {
                Color auroraColor = generateAuroraColor(i);
                AbstractDungeon.effectsQueue.add(
                        new LightningEffect(this.x + MathUtils.random(-100.0F, 100.0F),
                                this.y + MathUtils.random(-50.0F, 50.0F),
                                auroraColor));
            }

            // Add central light burst effect
            AbstractDungeon.effectsQueue.add(new LightFlareEffect(this.x, this.y, Color.CYAN, 2.4F));

            // Trigger screen shake
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.LONG, false);
        }

        // Decrease duration
        this.duration -= com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            CardCrawlGame.sound.stop("AURORA");
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void dispose() {}

    private Color generateAuroraColor(int id) {
        // Define the aurora colors
        switch (id % 6) { // Ensure the index wraps around if larger than 5
            case 0: return new Color(0.0F, 0.5F, 1.0F, 1.0F); // Blue
            case 1: return new Color(0.0F, 0.8F, 1.0F, 1.0F); // Cyan
            case 2: return new Color(0.0F, 0.6F, 0.7F, 1.0F); // Teal
            case 3: return new Color(0.0F, 0.4F, 0.5F, 1.0F); // Dark Teal
            case 4: return new Color(0.0F, 0.9F, 0.3F, 1.0F); // Green
            case 5: return new Color(0.8F, 0.2F, 0.7F, 1.0F); // Magenta
            default: return new Color(0.5F, 0.5F, 0.5F, 1.0F); // Fallback: Neutral Gray
        }
    }

}
