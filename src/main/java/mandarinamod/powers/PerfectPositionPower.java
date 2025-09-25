package mandarinamod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import mandarinamod.MandarinaMod;
import mandarinamod.util.CardUtils;

public class PerfectPositionPower extends BasePower {
    public static final String POWER_ID = MandarinaMod.makeID(PerfectPositionPower.class.getSimpleName());
    private static final String NAME = "Perfect Position";

    public PerfectPositionPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, true, owner, amount);
        this.name = NAME;
        updateDescription();
    }

    @Override
    public void onRemove() {
        // Ensure Perfect Position is deactivated when the power is removed
        CardUtils.setPerfectPosition(false);
    }
    @Override
    public void atEndOfTurn(boolean isPlayer){
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }
    @Override
    public void onVictory(){
        CardUtils.setPerfectPosition(false);
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
