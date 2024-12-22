package mandarinamod.cards.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.actions.unique.DiscardPileToTopOfDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;
import mandarinamod.util.ColorUtils;

public class Recall extends BaseCard {
    public static final String ID = makeID(Recall.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.SKILL, // This is a skill card
            CardRarity.BASIC, // Card is of basic rarity
            CardTarget.SELF, // Targets the player
            1 // Costs 1 energy
    );

    private static final int BLOCK = 7; // Base block provided by the card
    private static final int UPG_BLOCK = 3; // Block gained on upgrade

    public Recall() {
        super(ID, info);

        setBlock(BLOCK, UPG_BLOCK); // Set block and upgraded block value
        setMagic(1); // Set magic number to represent the number of cards to retrieve from discard pile
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block)); // Adds block to the player

        if (CardUtils.isOddPosition() || CardUtils.isPerfectPosition()) {
            addToBot(new DiscardPileToTopOfDeckAction(p)); // Add action to fetch one card from discard pile back to hand
        }

    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        //Default color
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

        // Even amount of cards played before
        if (CardUtils.isEvenPosition()) {
            // Set the glow color to gold if card would be even
            this.glowColor = ColorUtils.GREEN_GLOW.cpy();
        }
        if (CardUtils.isPerfectPosition()) {
            // Set the glow color to gold
            this.glowColor = GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Recall();
    }
}