package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.powers.ElectricPotentialPower;

public class ElectricPotential extends BaseCard {
    public static final String ID = MandarinaMod.makeID(ElectricPotential.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.POWER,            // Card type
            CardRarity.UNCOMMON,           // Rarity
            CardTarget.SELF,           // Targets self
            0                          // Costs 0 energy
    );

    public ElectricPotential() {
        super(ID, info);
        this.rawDescription = cardStrings.DESCRIPTION;
        setMagic(6,3);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Apply the Lightning Reprisal power to the player
        p.addPower(new ElectricPotentialPower(p, this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ElectricPotential();
    }
}
