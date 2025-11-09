package mandarinamod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.actions.CracklingArcAction;

import java.util.ArrayList;
import java.util.Collections;

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
            if (owner.hasPower(ThunderAspectPower.POWER_ID))  { //Allows to hit the rest of enemies besides the attacker
                ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
                for (int i = monsters.size() - 1; i >= 0; i--) {
                    AbstractMonster nextTarget = monsters.get(i);
                    if (!nextTarget.isDeadOrEscaped() && nextTarget != info.owner) {
                        this.addToTop(new DamageAction(
                                nextTarget,
                                new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS),
                                AttackEffect.LIGHTNING));
                    }
                }
            }
            this.addToTop(new DamageAction(
                    info.owner,
                    new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS),
                    AttackEffect.LIGHTNING));

            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
        return damageAmount;
    }
}
