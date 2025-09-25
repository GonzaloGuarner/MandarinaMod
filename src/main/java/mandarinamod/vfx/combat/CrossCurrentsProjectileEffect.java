package mandarinamod.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import mandarinamod.util.SoundManager;

public class CrossCurrentsProjectileEffect extends AbstractGameEffect {
    private final float startX, startY;
    private final float targetX, targetY;
    private float currentX, currentY;
    private final Color color;
    private float initialAngle;
    private float offset;
    private int index;
    private boolean hasPlayedSound = false;

    private static final String SOUND_ID = SoundManager.MEGASHWOOSH1;
    private static final String SOUND_ID2 = SoundManager.MEGASHWOOSH2;

    private static final TextureAtlas.AtlasRegion WIND_TEXTURE = ImageMaster.GLOW_SPARK;

    public CrossCurrentsProjectileEffect(float startX, float startY, AbstractMonster target, int index) {
        this.startX = startX;
        this.startY = startY;
        this.targetX = target.hb.cX;
        this.targetY = target.hb.cY;
        this.currentX = startX;
        this.currentY = startY;
        this.duration = 0.5F;
        this.startingDuration = 0.5F;
        this.color = Color.CYAN.cpy();
        this.index = index;
        initialAngle = MathUtils.atan2(targetY - startY, targetX - startX) * MathUtils.radDeg;
    }

    @Override
    public void update() {
        if (!hasPlayedSound && this.duration <= startingDuration * 0.5F) {
            CardCrawlGame.sound.play(index == 0 ? SOUND_ID : SOUND_ID2);
            hasPlayedSound = true;
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        float progress = 1f - (this.duration / this.startingDuration);

        float lerpX = Interpolation.linear.apply(startX, targetX, progress);
        float lerpY = Interpolation.linear.apply(startY, targetY, progress);

        float wobbleMagnitude = 30.0F * Settings.scale;
        float wobbleSpeed = 15.0F;

        offset = MathUtils.sin(progress * wobbleSpeed) * wobbleMagnitude * (index % 2 == 0 ? 1 : -1); // Opposite wobble for alternating projectiles

        float offsetX = offset * MathUtils.cos(initialAngle * MathUtils.degRad + MathUtils.PI / 2F);
        float offsetY = offset * MathUtils.sin(initialAngle * MathUtils.degRad + MathUtils.PI / 2F);

        this.currentX = lerpX + offsetX;
        this.currentY = lerpY + offsetY;

        this.scale = Interpolation.pow2In.apply(0.1F, 1.0F, progress) * 0.8F * Settings.scale;

        if (progress > 0.6F) {
            this.color.a = 1.0F - Interpolation.exp5In.apply(0.0F, 1.0F, (progress - 0.6F) / 0.4F);
        }

        if (this.duration <= 0F) {
            this.isDone = true;
            this.duration = 0.0F;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.draw(WIND_TEXTURE,
                this.currentX - WIND_TEXTURE.packedWidth / 2f,
                this.currentY - WIND_TEXTURE.packedHeight / 2f,
                WIND_TEXTURE.packedWidth / 2f,
                WIND_TEXTURE.packedHeight / 2f,
                WIND_TEXTURE.packedWidth,
                WIND_TEXTURE.packedHeight,
                this.scale, this.scale * 0.4F, // Stretch horizontally (X) and make thin (Y)
                this.initialAngle);
    }

    @Override
    public void dispose() { }
}
