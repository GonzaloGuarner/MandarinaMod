package mandarinamod.vfx.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;

public class ClawEffect extends AbstractGameEffect {
    public enum SlashDirection {
        DIAGONAL_UP,
        DIAGONAL_DOWN,
        DOWN
    }

    private float x;
    private float y;
    private SlashDirection direction;
    private Color color2;

    public ClawEffect(float x, float y, SlashDirection direction, Color color1, Color color2) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.color = color1;
        this.color2 = color2;
        this.startingDuration = 0.1F;
        this.duration = this.startingDuration;
    }

    public void update() {
        // Play sound effects
        if (MathUtils.randomBoolean()) {
            CardCrawlGame.sound.playA("ATTACK_DAGGER_5", MathUtils.random(0.0F, -0.3F));
        } else {
            CardCrawlGame.sound.playA("ATTACK_DAGGER_6", MathUtils.random(0.0F, -0.3F));
        }

        // Set offsets and angles based on the direction
        float[] xOffsets;
        float[] yOffsets;
        float angle;

        switch (direction) {
            case DIAGONAL_UP:
                xOffsets = new float[]{-35.0F, 0.0F, +35.0F};
                yOffsets = new float[]{-35.0F, 0.0F, +35.0F};
                angle = 45.0F; // Diagonal Up
                break;
            case DIAGONAL_DOWN:
                xOffsets = new float[]{-35.0F, 0.0F, -35.0F};
                yOffsets = new float[]{35.0F, 0.0F, +35.0F};
                angle = -45.0F; // Diagonal Down
                break;
            case DOWN:
                xOffsets = new float[]{-50.0F, 0.0F, 50.0F};
                yOffsets = new float[]{50.0F, 50.0F, 50.0F};
                angle = 180.0F; // Straight Down
                break;
            default:
                throw new IllegalArgumentException("Unknown direction: " + direction);
        }

        // Add the slashes with appropriate offsets and angles
        for (int i = 0; i < 3; i++) {
            AbstractDungeon.effectsQueue.add(new AnimatedSlashEffect(
                    this.x + xOffsets[i], this.y + yOffsets[i],
                    150.0F, -150.0F,
                    angle,
                    this.color,
                    this.color2));
        }

        this.isDone = true;
    }

    public void render(SpriteBatch sb) {
        // No rendering required since we're using `AnimatedSlashEffect` instances
    }

    public void dispose() {
    }
}
