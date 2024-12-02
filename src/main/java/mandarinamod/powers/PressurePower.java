package mandarinamod.powers;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import mandarinamod.MandarinaMod;

public class PressurePower extends BasePower {
    public static final String POWER_ID = MandarinaMod.makeID(PressurePower.class.getSimpleName());
    private static final String NAME = "Pressure";

    public PressurePower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.DEBUFF, true, owner, null, amount, true, false);
        this.loadRegion("pressure_points");
        this.name = NAME;
        this.updateDescription();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public void triggerPressures() {
        this.addToBot(new LoseHPAction(this.owner, null, this.amount, AttackEffect.FIRE));
    }
}
