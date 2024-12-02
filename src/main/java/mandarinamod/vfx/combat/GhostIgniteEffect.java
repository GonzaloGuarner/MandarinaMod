package mandarinamod.vfx.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;

public class GhostIgniteEffect extends AbstractGameEffect {
    private static final int COUNT = 25;
    private float x;
    private float y;

    public GhostIgniteEffect(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        for(int i = 0; i < 25; ++i) {
            AbstractDungeon.effectsQueue.add(new FireBurstParticleEffect(this.x, this.y));
            AbstractDungeon.effectsQueue.add(new LightFlareParticleEffect(this.x, this.y, Color.ORANGE));
        }

        this.isDone = true;
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
