package mandarinamod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import static mandarinamod.MandarinaMod.makeID;

public class VoidFormPower extends BasePower {
    public static final String POWER_ID = makeID(VoidFormPower.class.getSimpleName());

    public VoidFormPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.updateDescription();
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        // Modify damage dealt while in Shadow Form
        AbstractPower shadowAspect = owner.getPower(ShadowAspectPower.POWER_ID);
        if (shadowAspect != null) {
            return super.atDamageGive(damage, type) * 2.0F;
        } else {
            return super.atDamageGive(damage, type) * 1.5F;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // Remove Void Form at the end of turn
        if (isPlayer) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
            description = DESCRIPTIONS[0];
    }
}
