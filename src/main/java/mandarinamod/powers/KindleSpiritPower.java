package mandarinamod.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.tempcards.Spark;

public class KindleSpiritPower extends BasePower {
    public static final String POWER_ID = MandarinaMod.makeID(KindleSpiritPower.class.getSimpleName());
    private static final String NAME = "Kindle Spirit";

    public KindleSpiritPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.name = NAME;
        this.updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new MakeTempCardInHandAction(new Spark(), this.amount));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
