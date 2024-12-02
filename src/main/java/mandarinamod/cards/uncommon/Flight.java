package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.FlightPower;
import mandarinamod.util.CardStats;

public class Flight extends BaseCard {
    public static final String ID = MandarinaMod.makeID(Flight.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.SKILL,            // Card type
            CardRarity.UNCOMMON,       // Rarity
            CardTarget.SELF,           // Targets self
            1                          // Costs 1 energy
    );

    // Flight card constructor
    public Flight() {
        super(ID, info);
        setMagic(3, 1);
        setExhaust(true);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Apply FlightPower to the player
        addToBot(new ApplyPowerAction(p, p, new FlightPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Flight();
    }
}
