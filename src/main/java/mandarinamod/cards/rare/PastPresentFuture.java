package mandarinamod.cards.rare;


import com.megacrit.cardcrawl.actions.common.PutOnDeckAction;
import com.megacrit.cardcrawl.actions.unique.DiscardPileToTopOfDeckAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class PastPresentFuture extends BaseCard {
    public static final String ID = MandarinaMod.makeID(PastPresentFuture.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,      // Card color
            CardType.SKILL,
            CardRarity.RARE,    // Rarity
            CardTarget.NONE,
            0                   // Cost
    );

    private static final int SCRY_AMOUNT = 3;
    private static final int UPGRADE_SCRY_AMOUNT = 2;

    public PastPresentFuture() {
        super(ID, info);
        setMagic(SCRY_AMOUNT, UPGRADE_SCRY_AMOUNT);
        this.exhaust = true; // Exhausts after use
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        // Scry 3 (or 5 if upgraded)
        addToBot(new ScryAction(this.magicNumber));

        // Put a card from the hand onto the draw pile
        if (!p.hand.isEmpty()) {
            this.addToBot(new PutOnDeckAction(p, p, 1, false));
        }

        // Put a card from the discard pile onto the draw pile
        this.addToBot(new DiscardPileToTopOfDeckAction(p));

    }

    @Override
    public AbstractCard makeCopy() {
        return new PastPresentFuture();
    }
}

