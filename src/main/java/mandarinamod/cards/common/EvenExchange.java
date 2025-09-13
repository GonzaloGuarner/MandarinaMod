package mandarinamod.cards.common;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;
import mandarinamod.util.ColorUtils;

public class EvenExchange extends BaseCard {
    public static final String ID = MandarinaMod.makeID(EvenExchange.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.SKILL,              // Card type: Skill
            CardRarity.COMMON,         // Rarity: Common
            CardTarget.SELF,             // Target: Self
            1                            // Cost: 1 energy
    );

    private static final int DRAW = 2;              // Base draw
    private static final int UPGRADE_DRAW = 1;      // Upgraded draw
    private static final int EVEN_BONUS_DRAW = 1;   // Additional draw for Even position
    private static final int EXHAUST_AMOUNT = 1;    // Cards to exhaust in Even position

    public EvenExchange() {
        super(ID, info);
        setMagic(DRAW, UPGRADE_DRAW); // Sets the draw amount and its upgrade
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        // Basic effect: Draw cards
        addToBot(new DrawCardAction(player, this.magicNumber));

        // Bonus effect: If played in Even position, draw additional card and exhaust
        if (CardUtils.isEvenPosition() || CardUtils.isPerfectPosition()) {
            addToBot(new DrawCardAction(player, EVEN_BONUS_DRAW));
            addToBot(new ExhaustAction(player, player, EXHAUST_AMOUNT, false));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new EvenExchange();
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        //Default color
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

        if (CardUtils.isOddPosition()) {
            // Set the glow color to magenta if card would be even
            this.glowColor = ColorUtils.MAGENTA_GLOW.cpy();
        }
        if (CardUtils.isPerfectPosition()) {
            // Set the glow color to gold
            this.glowColor = ColorUtils.GOLD_GLOW.cpy();
        }
    }
}

