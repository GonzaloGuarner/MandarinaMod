package mandarinamod.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import mandarinamod.cards.tempcards.Spark;
import com.megacrit.cardcrawl.core.AbstractCreature;
import mandarinamod.MandarinaMod;

public class ElectrifyingBarrierPower extends BasePower {
    public static final String POWER_ID = MandarinaMod.makeID(ElectrifyingBarrierPower.class.getSimpleName());
    private static final String NAME = "Electrifying Barrier";
    private int accumulatedBlockThisTurn; // Tracks the total block gained this turn
    private int threshold;
    private int sparksToGenerate;

    public ElectrifyingBarrierPower(AbstractCreature owner, int amount, int threshold) {
        super(POWER_ID, PowerType.BUFF, true, owner, amount);
        this.name = NAME;
        this.threshold = threshold;
        this.accumulatedBlockThisTurn = 0;
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        accumulatedBlockThisTurn = owner.currentBlock;
    }

    @Override
    public void onGainedBlock(float blockAmount) {
        // Accumulate block gained each time block is added
        accumulatedBlockThisTurn += (int)blockAmount;
    }
//    @Override
//    public void atEndOfTurn(boolean isPlayer) {
//        if (!isPlayer) return;
//        this.flash();
//        accumulatedBlockThisTurn = owner.currentBlock;
//    }

    @Override
    public void atEndOfRound() {
        int blockedDamage = accumulatedBlockThisTurn - owner.currentBlock;
        if(blockedDamage < 0){
            int i = 1;
        }
        // Calculate Sparks to generate based on accumulated block and threshold
        sparksToGenerate = calculateSparks(blockedDamage);

        if (sparksToGenerate > 0) {
            this.flash();
            // Add Sparks to the hand
            for (int i = 0; i < sparksToGenerate; i++) {
                this.addToBot(new MakeTempCardInHandAction(new Spark()));
            }
        }

        // Reset accumulated block for the next turn
        accumulatedBlockThisTurn = 0;
        reduceStacks();
        updateDescription();
    }
    public void reduceStacks() {
        if (this.amount <= 1) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.POWER_ID));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this.POWER_ID, 1));
        }
    }
    private int calculateSparks(int blockedDamage) {
        // Calculate the number of Sparks generated based on the accumulated block and the threshold
        return blockedDamage / threshold;
    }

    @Override
    public void updateDescription() {
        // Update the description to reflect the threshold for generating Sparks
        this.description = "Next turn, gain 1 Spark for every block gained this turn.";
    }

    @Override
    public void onRemove() {
        // When the power is removed, reset the accumulated block
        accumulatedBlockThisTurn = 0;
    }
}