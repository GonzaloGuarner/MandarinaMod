package mandarinamod.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import mandarinamod.MandarinaMod;

public class BurntPower extends BasePower implements HealthBarRenderPower {
    public static final String POWER_ID = MandarinaMod.makeID(BurntPower.class.getSimpleName());
    private static final String NAME = "Burnt";
    private static final Color burntColor = new Color(242F / 255.0F, 82F / 255.0F, 12F / 255.0F, 1f);

    public BurntPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.DEBUFF, true, owner, amount);
        this.name = NAME;
        this.updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        // Deal damage equal to the current stack amount
        addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.FIRE));
        // Halve the burn stacks (rounding down)
        this.amount = this.amount / 2;
        if (this.amount == 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public int getHealthBarAmount() {
        return this.amount;
    }

    @Override
    public Color getColor() {
        return burntColor;
    }
}