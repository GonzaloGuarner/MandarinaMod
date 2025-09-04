package mandarinamod.monsters;

import basemod.BaseMod;
import basemod.animations.AbstractAnimation;
import basemod.animations.SpineAnimation;
import basemod.interfaces.ModelRenderSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.esotericsoftware.spine.AnimationStateData;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;


public class PartnerMonster extends AbstractMonster implements ModelRenderSubscriber {
    private static final MethodHandle renderNameHandle;
    public AbstractAnimation animation;
    public boolean renderCorpse;
    public float scale;
    public Texture corpseImg;
    public float target_x;
    public float target_y;
    protected Texture[] attackIntents;
    private boolean takenTurn;

    public PartnerMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, (String)null, offsetX, offsetY);
        this.renderCorpse = false;
        this.scale = 1.0F;
        this.corpseImg = null;
        this.takenTurn = false;
        if (imgUrl != null) {
            this.img = new Texture(imgUrl);
        }

        this.target_x = this.drawX;
        this.target_y = this.drawY;
    }

    public PartnerMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY, Texture[] attackIntents) {
        this(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
        this.attackIntents = attackIntents;
    }


    public void setTakenTurn(boolean takenTurn) {
        this.takenTurn = takenTurn;
    }

    public boolean hasTakenTurn() {
        return this.takenTurn;
    }

    public void updateAnimations() {
        this.drawX = MathHelper.orbLerpSnap(this.drawX, this.target_x);
        this.drawY = MathHelper.orbLerpSnap(this.drawY, this.target_y);
        super.updateAnimations();
    }

    public void applyEndOfTurnTriggers() {
        super.applyEndOfTurnTriggers();
        this.takenTurn = false;
    }

    public void renderCustomTips(SpriteBatch sb, ArrayList<PowerTip> tips) {
    }

    public Texture[] getAttackIntents() {
        return this.attackIntents;
    }

    public void takeTurn() {
    }

    protected void getMove(int i) {
    }

    public void die() {
        this.die(true);
    }

    public void die(boolean triggerPowers) {
        if (!this.isDying) {
            this.isDying = true;
            if (this.currentHealth <= 0 && triggerPowers) {
                Iterator var2 = this.powers.iterator();

                while(var2.hasNext()) {
                    AbstractPower p = (AbstractPower)var2.next();
                    p.onDeath();
                }
            }

            if (this.currentHealth < 0) {
                this.currentHealth = 0;
            }

            if (!Settings.FAST_MODE) {
                ++this.deathTimer;
            } else {
                ++this.deathTimer;
            }
        }

    }

    protected void loadAnimation(AbstractAnimation animation) {
        this.animation = animation;
        if (animation instanceof SpineAnimation) {
            SpineAnimation spine = (SpineAnimation)animation;
            this.loadAnimation(spine.atlasUrl, spine.skeletonUrl, spine.scale);
        }

        if (animation.type() != AbstractAnimation.Type.NONE) {
            this.atlas = new TextureAtlas();
        }

        if (animation.type() == AbstractAnimation.Type.MODEL) {
            BaseMod.subscribe(this);
        }

    }

    public AnimationStateData getStateData() {
        return this.stateData;
    }

    public void playDeathAnimation() {
        if (this.corpseImg != null) {
            this.img = this.corpseImg;
            this.renderCorpse = true;
        }

    }

    public void dispose() {
        super.dispose();
        if (this.corpseImg != null) {
            this.corpseImg.dispose();
            this.corpseImg = null;
        }

    }

    public void update() {
        super.update();

    }

    public void render(SpriteBatch sb) {
        if (!this.isDead && !this.escaped) {
            if (this.atlas != null && !this.renderCorpse) {
                switch (this.animation.type()) {
                    case NONE:
                        this.state.update(Gdx.graphics.getDeltaTime());
                        this.state.apply(this.skeleton);
                        this.skeleton.updateWorldTransform();
                        this.skeleton.setPosition(this.drawX + this.animX, this.drawY + this.animY);
                        this.skeleton.setColor(this.tint.color);
                        this.skeleton.setFlip(this.flipHorizontal, this.flipVertical);
                        sb.end();
                        CardCrawlGame.psb.begin();
                        sr.draw(CardCrawlGame.psb, this.skeleton);
                        CardCrawlGame.psb.end();
                        sb.begin();
                        sb.setBlendFunction(770, 771);
                        break;
                    case MODEL:
                        BaseMod.publishAnimationRender(sb);
                        break;
                    case SPRITE:
                        this.animation.setFlip(this.flipHorizontal, this.flipVertical);
                        this.animation.renderSprite(sb, this.drawX + this.animX, this.drawY + this.animY + AbstractDungeon.sceneOffsetY);
                }
            } else {
                sb.setColor(this.tint.color);
                if (this.img != null) {
                    this.drawStaticImg(sb);
                }
            }

            if (this == AbstractDungeon.getCurrRoom().monsters.hoveredMonster && this.atlas == null) {
                sb.setBlendFunction(770, 1);
                sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.1F));
                if (this.img != null) {
                    this.drawStaticImg(sb);
                    sb.setBlendFunction(770, 771);
                }
            }

            this.hb.render(sb);
            this.intentHb.render(sb);
            this.healthHb.render(sb);
        }

        if (!AbstractDungeon.player.isDead) {
            this.renderHealth(sb);

            try {
                renderNameHandle.invoke(this, sb);
            } catch (Throwable var3) {
                Throwable e = var3;
                e.printStackTrace();
            }
        }
    }

    private void drawStaticImg(SpriteBatch sb) {
        sb.draw(this.img, this.drawX - (float)this.img.getWidth() * this.scale * Settings.scale / 2.0F + this.animX, this.drawY + this.animY, (float)this.img.getWidth() * this.scale * Settings.scale, (float)this.img.getHeight() * this.scale * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
    }

    public void receiveModelRender(ModelBatch batch, Environment env) {
        this.animation.renderModel(batch, env);
    }

    static {
        MethodHandle tmpHandle;
        try {
            Method renderMethod = AbstractMonster.class.getDeclaredMethod("renderName", SpriteBatch.class);
            renderMethod.setAccessible(true);
            tmpHandle = MethodHandles.publicLookup().unreflect(renderMethod);
        } catch (NoSuchMethodException | IllegalAccessException var2) {
            ReflectiveOperationException e = var2;
            ((ReflectiveOperationException)e).printStackTrace();
            tmpHandle = null;
        }

        renderNameHandle = tmpHandle;
    }
}
