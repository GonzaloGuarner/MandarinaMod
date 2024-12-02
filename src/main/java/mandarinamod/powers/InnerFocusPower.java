package mandarinamod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;

import static mandarinamod.MandarinaMod.makeID;

public class InnerFocusPower extends BasePower {
    public static final String POWER_ID = makeID(InnerFocusPower.class.getSimpleName());

    public InnerFocusPower(AbstractCreature owner, int amount) {
        // Call the constructor of BasePower to initialize
        super(POWER_ID, PowerType.BUFF, true, owner,null, amount, true, true);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // Ensure that this is only called at the end of the player's turn
        if (isPlayer && owner.currentBlock == 0) {
            flash(); // Visual feedback that the power is activating
            // Add card draw action to the queue
            addToBot(new ApplyPowerAction(owner, owner, new DrawCardNextTurnPower(owner, this.amount), this.amount));
        }
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }
}