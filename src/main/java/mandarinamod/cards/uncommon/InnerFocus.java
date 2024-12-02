package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.InnerFocusPower;
import mandarinamod.util.CardStats;

public class InnerFocus extends BaseCard {
    public static final String ID = makeID(InnerFocus.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.POWER,            // This is a power card
            CardRarity.UNCOMMON,       // Card rarity
            CardTarget.SELF,           // Targets the player
            1                          // Costs 1 energy
    );

    private static final int EXTRA_DRAW = 1;           // Base extra card draw amount
    private static final int UPGRADED_EXTRA_DRAW = 1;  // Additional card draw on upgrade

    // Load card strings
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    public InnerFocus() {
        super(ID, info);
        setMagic(EXTRA_DRAW, UPGRADED_EXTRA_DRAW); // Set the amount of extra cards to draw
        this.rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Apply InnerFocusPower to the player
        addToBot(new ApplyPowerAction(p, p, new InnerFocusPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADED_EXTRA_DRAW); // Increase the extra draw amount
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new InnerFocus();
    }
}
