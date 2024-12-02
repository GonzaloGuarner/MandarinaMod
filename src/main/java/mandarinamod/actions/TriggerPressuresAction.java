package mandarinamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import mandarinamod.MandarinaMod;
import mandarinamod.powers.PressurePower;

import java.util.Iterator;
import java.util.Objects;

public class TriggerPressuresAction extends AbstractGameAction {
    AbstractCard card;

    public TriggerPressuresAction() {
    }

    public void update() {
        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster mo = (AbstractMonster)var1.next();
            Iterator var3 = mo.powers.iterator();

            while(var3.hasNext()) {
                AbstractPower p = (AbstractPower)var3.next();
                if (Objects.equals(p.ID, PressurePower.POWER_ID))
                {
                    PressurePower pp = (PressurePower) p;
                    pp.triggerPressures();
                }
            }
        }

        this.isDone = true;
    }
}

