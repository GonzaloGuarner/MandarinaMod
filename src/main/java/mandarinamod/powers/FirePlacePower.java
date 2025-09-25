package mandarinamod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static mandarinamod.MandarinaMod.makeID;

public class FirePlacePower extends BasePower {
    public static final String POWER_ID = makeID(FirePlacePower.class.getSimpleName());

    public FirePlacePower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, true, owner, amount);
        this.updateDescription();
    }


    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        AbstractCreature monster = info.owner;
        if (monster != null && monster != this.owner) {
            this.flash();
            // Deal damage back to the attacker
            this.addToTop(new ApplyPowerAction(monster, this.owner, new BurntPower(monster, this.amount), this.amount));
        }
        return damageAmount;
    }
    public void atStartOfTurn() {
        this.addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, POWER_ID));
    }
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
