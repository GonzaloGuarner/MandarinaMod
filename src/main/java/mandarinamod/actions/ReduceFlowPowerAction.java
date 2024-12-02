package mandarinamod.actions;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import mandarinamod.powers.FlowPower;
import mandarinamod.powers.FlowStatePower;

public class ReduceFlowPowerAction extends ReducePowerAction {

    public ReduceFlowPowerAction(AbstractCreature target, AbstractCreature source, String powerID, int reduceAmount) {
        super(target, source, powerID, reduceAmount);
    }

    @Override
    public void update() {

        if (this.duration == this.startDuration) {
            AbstractPower power = this.target.getPower(FlowPower.POWER_ID);
            // Check if the power is FlowPower
            if (power instanceof FlowPower) {
                // Trigger custom behavior in FlowStatePower if present
                AbstractPower flowState = this.target.getPower(FlowStatePower.POWER_ID);
                if (flowState instanceof FlowStatePower) {
                    ((FlowStatePower) flowState).onReducePower();
                }
            }
        }

        // Call the original logic
        super.update();
    }
}
