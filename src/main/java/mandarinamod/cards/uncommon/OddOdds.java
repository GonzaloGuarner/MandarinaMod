package mandarinamod.cards.uncommon;


import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.cards.pseudocards.AttackGuessChoice;
import mandarinamod.cards.pseudocards.SkillGuessChoice;
import mandarinamod.cards.pseudocards.NeitherGuessChoice;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;
import mandarinamod.util.ColorUtils;

import java.util.ArrayList;

public class OddOdds extends BaseCard {
    public static final String ID = MandarinaMod.makeID(OddOdds.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color
            CardType.SKILL,              // Card type
            CardRarity.UNCOMMON,             // Rarity
            CardTarget.NONE,             // Target
            1                            // Cost
    );

    public OddOdds() {
        super(ID, info);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        ArrayList<AbstractCard> options = new ArrayList<>();
        options.add(new AttackGuessChoice());
        options.add(new SkillGuessChoice());
        options.add(new NeitherGuessChoice());
        addToBot(new ChooseOneAction(options));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.cost = 0;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new OddOdds();
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        //Default color
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

        // Odd amount of cards played before
        if (CardUtils.isOddPosition()) {
            // Set the glow color to magenta if card would be even
            this.glowColor = ColorUtils.MAGENTA_GLOW.cpy();
        }
        if (CardUtils.isEvenPosition()) {
            // Set the glow color to green if card would be odd
            this.glowColor = ColorUtils.GREEN_GLOW.cpy();
        }
        if (CardUtils.isPerfectPosition()) {
            // Set the glow color to gold
            this.glowColor = ColorUtils.GOLD_GLOW.cpy();
        }
    }
}
