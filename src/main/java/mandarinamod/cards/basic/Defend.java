package mandarinamod.cards.basic;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class Defend extends BaseCard {
    public static final String ID = makeID(Defend.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // The card color associated with Mandarina's character
            CardType.SKILL, // Card type is SKILL since it provides block
            CardRarity.BASIC, // Starting card rarity
            CardTarget.SELF, // Targets the player (SELF)
            1 // Base energy cost
    );

    private static final int BLOCK = 5; // Base block value
    private static final int UPG_BLOCK = 3; // Additional block on upgrade

    public Defend() {
        super(ID, info);

        setBlock(BLOCK, UPG_BLOCK); // Set the block value and the upgrade amount
        tags.add(CardTags.STARTER_DEFEND); // Marks it as a basic Defend card for interactions with relics or other cards
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block)); // Adds a GainBlock action that targets the player and uses the current block value
    }

    @Override
    public AbstractCard makeCopy() {
        return new Defend();
    }
}