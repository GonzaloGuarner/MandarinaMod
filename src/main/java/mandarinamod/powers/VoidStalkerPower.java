package mandarinamod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import static mandarinamod.MandarinaMod.makeID;

public class VoidStalkerPower extends BasePower {
    public static final String POWER_ID = makeID(VoidStalkerPower.class.getSimpleName());
    private static final int TURN_THRESHOLD = 3;
    private int turnsElapsed;

    public VoidStalkerPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.turnsElapsed = 0; // Start counting turns from 0
        this.updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        // Increment the turn counter at the start of each player's turn
        turnsElapsed++;
        flash();
        this.amount = TURN_THRESHOLD - turnsElapsed; // Update amount to show the remaining turns
        this.updateDescription();

        // Check if we have reached the turn threshold
        if (turnsElapsed >= TURN_THRESHOLD) {
            flash();
            addToBot(new ApplyPowerAction(owner, owner, new IntangiblePlayerPower(owner, 1), 1));
            addToBot(new ApplyPowerAction(owner, owner, new VoidFormPower(owner, 1), 1));
            // Reset turnsElapsed after transformation
            turnsElapsed = 0;
            this.amount = TURN_THRESHOLD; // Reset amount to initial value
            this.updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        if(this.amount <= 1){
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }else{
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        }

    }
}