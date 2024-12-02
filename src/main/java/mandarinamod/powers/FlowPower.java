package mandarinamod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import mandarinamod.MandarinaMod;
import mandarinamod.actions.ReduceFlowPowerAction;

public class FlowPower extends BasePower {
    public static final String POWER_ID = MandarinaMod.makeID(FlowPower.class.getSimpleName());

    public FlowPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, true, owner, null, amount, true, true);
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount >= 3) {
            flash();
            addToBot(new DrawCardAction(owner, 1));
            addToBot(new ReduceFlowPowerAction(owner, owner, POWER_ID, 1));
        }
    }

    @Override
    public void updateDescription() {
        if (this.amount >= 3) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }
    }
}

