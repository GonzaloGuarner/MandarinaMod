package mandarinamod.powers;

import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import static mandarinamod.MandarinaMod.makeID;

public class ThunderAspectPower extends BasePower {
    public static final String POWER_ID = makeID(ThunderAspectPower.class.getSimpleName());

    public ThunderAspectPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new DamageAllEnemiesAction(owner, DamageInfo.createDamageMatrix(this.amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.LIGHTNING));
    }

    @Override
    public void updateDescription() {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
