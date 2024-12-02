package mandarinamod.cards.basic;

import com.megacrit.cardcrawl.actions.common.BetterDiscardPileToHandAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class Meditate extends BaseCard {
    public static final String ID = makeID(Meditate.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.SKILL, // This is a skill card
            CardRarity.BASIC, // Card is of basic rarity
            CardTarget.SELF, // Targets the player
            1 // Costs 1 energy
    );

    private static final int BLOCK = 6; // Base block provided by the card
    private static final int UPG_BLOCK = 4; // Block gained on upgrade

    public Meditate() {
        super(ID, info);

        setBlock(BLOCK, UPG_BLOCK); // Set block and upgraded block value
        setMagic(1); // Set magic number to represent the number of cards to retrieve from discard pile
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block)); // Adds block to the player
        addToBot(new BetterDiscardPileToHandAction(1)); // Add action to fetch one card from discard pile back to hand
    }

    @Override
    public AbstractCard makeCopy() {
        return new Meditate();
    }
}