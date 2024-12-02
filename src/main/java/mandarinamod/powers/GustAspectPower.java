package mandarinamod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import mandarinamod.MandarinaMod;

public class GustAspectPower extends BasePower {
    public static final String POWER_ID = MandarinaMod.makeID(GustAspectPower.class.getSimpleName());


    public GustAspectPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, true, owner,null, amount, true, true);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            // Apply Flow to the player
            flash();
            addToBot(new ApplyPowerAction(owner, owner, new FlowPower(owner, amount), amount));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
