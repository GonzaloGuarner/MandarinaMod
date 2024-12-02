package mandarinamod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import static mandarinamod.MandarinaMod.makeID;

public class FlameAspectPower extends BasePower {
    public static final String POWER_ID = makeID(FlameAspectPower.class.getSimpleName());

    private int startingIncrement; // Sum of starting increments from all instances
    private int incrementStep;     // Sum of increment steps from all instances
    private int currentIncrement;  // Total increment applied to the next attack

    public FlameAspectPower(AbstractCreature owner, int startingIncrement, int incrementStep) {
        super(POWER_ID, PowerType.BUFF, false, owner, 0);
        this.startingIncrement = startingIncrement;
        this.incrementStep = incrementStep;
        this.currentIncrement = this.startingIncrement;
        this.amount = this.currentIncrement; // Display current increment
        this.amount2 = this.incrementStep;   // Display increment step
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        // Since stackAmount carries both startingIncrement and incrementStep, we need to handle stacking accordingly
        // However, in the default stackPower method, stackAmount is a single int. We'll need to override how stacking works
        // We'll handle stacking in the ApplyPowerAction instead
        super.stackPower(stackAmount);
        updateDescription();
    }

    // Custom method to handle stacking with multiple parameters
    public void stackPower(int additionalStartingIncrement, int additionalIncrementStep) {
        this.fontScale = 8.0f;

        this.startingIncrement += additionalStartingIncrement;
        this.incrementStep += additionalIncrementStep;
        this.currentIncrement = this.startingIncrement;

        this.amount = this.currentIncrement; // Update display
        this.amount2 = this.incrementStep;

        updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            // Apply the current increment to the attack's base damage
            card.baseDamage += this.currentIncrement;
            card.applyPowers(); // Recalculate damage with powers
            card.flash(); // Visual feedback on the card
            this.flash(); // Visual feedback on the power

            // Increase the current increment by the increment step
            this.currentIncrement += this.incrementStep;
            this.amount = this.currentIncrement; // Update display
            updateDescription();
        }
    }

    @Override
    public void atStartOfTurn() {
        // Reset the current increment at the start of each turn
        this.currentIncrement = this.startingIncrement;
        this.amount = this.currentIncrement;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount2 + DESCRIPTIONS[2];
    }
}
