package mandarinamod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static mandarinamod.MandarinaMod.makeID;

public class StaticPower extends BasePower {
    public static final String POWER_ID = makeID(StaticPower.class.getSimpleName());

    public StaticPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        if (this.owner.hasPower(ThunderAspectPower.POWER_ID)) {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        // Check if the attacker is not null and not the owner
        if (info.owner != null && info.owner != this.owner) {
            this.flash();
            // Deal damage back to the attacker
            this.addToTop(new DamageAction(
                    info.owner,
                    new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS),
                    AttackEffect.LIGHTNING));
            // Remove this power after it triggers once
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
        return damageAmount;
    }
}
