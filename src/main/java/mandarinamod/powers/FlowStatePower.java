package mandarinamod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import mandarinamod.MandarinaMod;

import java.util.Objects;

public class FlowStatePower extends BasePower implements OnReceivePowerPower {
    public static final String POWER_ID = MandarinaMod.makeID(FlowStatePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private int damageOnFlowGain;
    private int flowGain;

    public FlowStatePower(AbstractCreature owner, int damageOnFlowGain) {
        super(POWER_ID, PowerType.BUFF, false, owner, damageOnFlowGain);
        this.damageOnFlowGain = damageOnFlowGain;
        this.updateDescription();
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (target == this.owner && Objects.equals(power.ID, FlowPower.POWER_ID)) {
            damageRandom();
        }
        return true; // Allow the power to be received as normal
    }

    public void onReducePower() {
        damageRandom();
    }

    public void damageRandom() {
        // Deal damage to a random enemy
        AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(true);
        if (m != null) {
            addToBot(new DamageAction(m, new DamageInfo(owner, damageOnFlowGain, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));

        }
    }

    @Override
    public void updateDescription() {
        description = String.format(powerStrings.DESCRIPTIONS[0], damageOnFlowGain, flowGain);
    }


}
