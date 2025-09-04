package mandarinamod.powers;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import mandarinamod.MandarinaMod;

public class BurntPower extends BasePower {
    public static final String POWER_ID = MandarinaMod.makeID(BurntPower.class.getSimpleName());
    private static final String NAME = "Burnt";

    public BurntPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.DEBUFF, true, owner, amount);
        this.name = NAME;
        this.updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        flash();
        // Deal damage equal to the current stack amount
        addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        // Halve the burn stacks (rounding down)
        this.amount = this.amount / 2;
        if (this.amount == 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.POWER_ID));
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}