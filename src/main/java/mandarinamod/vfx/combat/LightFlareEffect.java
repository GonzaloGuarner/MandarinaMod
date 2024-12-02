package mandarinamod.vfx.combat;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class LightFlareEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float scale;
    private Color color;

    public LightFlareEffect(float x, float y, Color color, float scale) {
        this.x = x;
        this.y = y;
        this.color = color.cpy();
        this.scale = scale;
        this.duration = 0.6F; // Total duration of the effect
        this.startingDuration = 0.6F;
    }

    @Override
    public void update() {
        // Fade out effect as duration decreases
        this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / this.startingDuration);
        this.scale += Gdx.graphics.getDeltaTime() * 2.0F; // Expand the flare as time progresses
        this.duration -= Gdx.graphics.getDeltaTime();

        if (this.duration < 0.0F) {
            this.isDone = true; // Mark the effect as complete
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        sb.draw(ImageMaster.CARD_FLASH_VFX, this.x - 64.0F, this.y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, this.scale, this.scale, MathUtils.random(0, 360));
        sb.setBlendFunction(770, 771); // Restore normal blend mode
    }

    @Override
    public void dispose() {}
}
