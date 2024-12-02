package mandarinamod.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.GiantFireEffect;

public class EruptEffect extends AbstractGameEffect {
    private float timer = 0.0F;
    private static final float INTERVAL = 0.05F;
    private int fireAmount = 0;
    public EruptEffect(int times, int fireAmount) {
        this.duration = 0.15F+times*INTERVAL;
        this.startingDuration = this.duration;
        this.fireAmount = fireAmount;
    }

    public void update() {
        if (this.duration == this.startingDuration) {
            CardCrawlGame.sound.playV("GHOST_FLAMES",1.0f);
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.ORANGE));
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        this.timer -= Gdx.graphics.getDeltaTime();
        if (this.timer < 0.0F) {
            for (int i = 0; i < fireAmount; i++) {
                AbstractDungeon.effectsQueue.add(new GiantFireEffect());
            }
            this.timer = 0.05F;
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
