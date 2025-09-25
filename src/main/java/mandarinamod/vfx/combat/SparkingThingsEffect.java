package mandarinamod.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ImpactSparkEffect;

public class SparkingThingsEffect extends AbstractGameEffect {
    private final float x;
    private final float y;
    private final int numSparks;
    private boolean hasFired = false;

    public SparkingThingsEffect(float x, float y, int numSparks) {
        this.x = x;
        this.y = y;
        this.numSparks = numSparks;
        this.duration = 0.05F; // A very short duration to trigger the effect immediately
        this.startingDuration = this.duration;
    }

    @Override
    public void update() {
        if (!hasFired) {
            hasFired = true;

            for (int i = 0; i < this.numSparks*5; i++) {
                AbstractDungeon.effectsQueue.add(new ImpactSparkEffect(
                        x + MathUtils.random(-150.0F, 150.0F) * Settings.scale,
                        y + MathUtils.random(-150.0F, 150.0F) * Settings.scale
                ));
            }
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
    }

    @Override
    public void dispose() {
    }
}