package mandarinamod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import mandarinamod.MandarinaMod;

public class ThunderClapped extends BasePower {
    public static final String POWER_ID = MandarinaMod.makeID(ThunderClapped.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private static final int DAMAGE_TO_ALL = 2;

    public ThunderClapped(AbstractCreature owner, AbstractCreature source) {
        super(POWER_ID, PowerType.DEBUFF, false, owner, source, DAMAGE_TO_ALL);
        this.loadRegion("talk_to_hand");
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        // Trigger the effect only if the marked target is attacked
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner) {
            this.flash();
            addToTop(new DamageAllEnemiesAction(this.source, DamageInfo.createDamageMatrix(this.amount, true),
                    DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.LIGHTNING));
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }
}
