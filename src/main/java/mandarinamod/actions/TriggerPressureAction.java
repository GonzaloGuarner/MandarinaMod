package mandarinamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import mandarinamod.MandarinaMod;
import mandarinamod.powers.BasePower;
import mandarinamod.powers.PressurePower;

import java.util.Iterator;
import java.util.Objects;

public class TriggerPressureAction extends AbstractGameAction {
    AbstractMonster target;
    public TriggerPressureAction(AbstractMonster m) {
        this.target = m;
    }

    public void update() {;
        Iterator var3 = target.powers.iterator();

        while(var3.hasNext()) {
            AbstractPower p = (AbstractPower)var3.next();
            if (Objects.equals(p.ID, PressurePower.POWER_ID))
            {
                PressurePower pp = (PressurePower) p;
                pp.triggerPressures();
            }
        }

        this.isDone = true;
    }
}
