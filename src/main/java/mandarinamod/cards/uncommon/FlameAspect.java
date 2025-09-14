package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.FlameAspectPower;
import mandarinamod.util.CardStats;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class FlameAspect extends BaseCard {
    public static final String ID = makeID(FlameAspect.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int STARTING_INCREMENT = 0;
    private static final int UPGRADE_PLUS_STARTING_INCREMENT = 1;
    private static final int INCREMENT_STEP = 1;

    public FlameAspect() {
        super(ID, info);
        setMagic(INCREMENT_STEP, 0);
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int startingIncrement = STARTING_INCREMENT;
        if (upgraded) {
            startingIncrement += UPGRADE_PLUS_STARTING_INCREMENT;
        }
        // Check if the player already has FlameAspectPower
        if (p.hasPower(FlameAspectPower.POWER_ID)) {
            FlameAspectPower existingPower = (FlameAspectPower) p.getPower(FlameAspectPower.POWER_ID);
            // Stack the power using the custom method
            existingPower.stackPower(startingIncrement, this.magicNumber);
        } else {
            // Apply the power with both starting increment and increment step
            FlameAspectPower newPower = new FlameAspectPower(p, startingIncrement, this.magicNumber);
            addToBot(new ApplyPowerAction(p, p, newPower, 1));
        }

        addToBot(new VFXAction(new FlameBarrierEffect(p.hb.cX, p.hb.cY)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlameAspect();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(0);
            // No change to magicNumber (increment step remains the same)
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
