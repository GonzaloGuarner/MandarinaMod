package mandarinamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.powers.PressurePower;

public class PressureReliefAction extends AbstractGameAction {
        private final AbstractMonster m;
        private final AbstractPlayer p;

        public PressureReliefAction(AbstractMonster m, AbstractPlayer p, int amount) {
            this.m = m;
            this.p = p;
            this.amount = amount;
            this.actionType = ActionType.WAIT;
            this.duration = this.startDuration = Settings.ACTION_DUR_MED;
        }

        @Override
        public void update() {
            //addToBot(new ApplyPowerAction(m, p, new PressurePower(m, amount), amount));
            if (this.duration == Settings.ACTION_DUR_MED) {
                //Apply Pressure to target
                addToBot(new ApplyPowerAction(m, p, new PressurePower(m, amount), amount));

                // Add TriggerPressuresAction twice to the action queue
                TriggerPressuresAction firstTrigger = new TriggerPressuresAction();
                TriggerPressuresAction secondTrigger = new TriggerPressuresAction();

                addToBot(firstTrigger);
                addToBot(secondTrigger);
                addToBot(new WaitAction(0.5f));
                // Update the duration to simulate waiting for those actions to complete
                //this.duration = Settings.ACTION_DUR_MED; // Adjust this duration if needed
            } else if (this.duration <= 0.1f) {
                // Actions have completed, now we remove PressurePower

                    addToBot(new RemoveSpecificPowerAction(m, p, PressurePower.POWER_ID));

                this.isDone = true;
            }

            this.tickDuration();
        }

}
