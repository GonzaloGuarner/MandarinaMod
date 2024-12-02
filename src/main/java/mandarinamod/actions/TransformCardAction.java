package mandarinamod.actions;


import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class TransformCardAction extends AbstractGameAction {
    private AbstractCard original;
    private AbstractCard replacement;

    public TransformCardAction(AbstractCard original, AbstractCard replacement) {
        this.original = original;
        this.replacement = replacement;
        if (Settings.FAST_MODE) {
            this.startDuration = 0.05F;
        } else {
            this.startDuration = 0.15F;
        }

        this.duration = this.startDuration;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            this.replacement.current_x = original.current_x;
            this.replacement.current_y = original.current_y;
            this.replacement.target_x = original.target_x;
            this.replacement.target_y = original.target_y;
            this.replacement.drawScale = 1.0F;
            this.replacement.targetDrawScale = original.targetDrawScale;
            this.replacement.angle = original.angle;
            this.replacement.targetAngle = original.targetAngle;
            this.replacement.superFlash(Color.WHITE.cpy());
            AbstractDungeon.player.hand.group.add(this.replacement);
            AbstractDungeon.player.hand.glowCheck();
        }

        this.tickDuration();
    }
}

