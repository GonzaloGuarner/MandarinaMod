package mandarinamod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mandarinamod.MandarinaMod;

public class ElectricPotentialPower extends BasePower {
    public static final String POWER_ID = MandarinaMod.makeID(ElectricPotentialPower.class.getSimpleName());

    public ElectricPotentialPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        this.flash(); // Visual effect to indicate that the power has triggered
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        // If the player is hit, gain an amount of Static Charge equivalent to this power's amount.
        if (damageAmount > 0 && info.owner != null) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner,
                    new StaticPower(this.owner, this.amount), this.amount));
            this.flash(); // Show the visual effect when it triggers
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
