package mandarinamod.cards.tempcards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DescriptionLine;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.cards.uncommon.SparkingThings;
import mandarinamod.util.CardStats;

import java.util.regex.Pattern;

public class FuelReserve extends BaseCard {
    public static final String ID = MandarinaMod.makeID(FuelReserve.class.getSimpleName());
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            1
    );

    private static final int ENERGY = 1;
    private static final int UPGRADE_ENERGY = 1;
    private boolean isPreview;
    private AbstractCard sparkingThings;

    public FuelReserve(boolean isPreview) {
        super(ID, info);
        setMagic(ENERGY);
        FleetingField.fleeting.set(this, true);
        this.isPreview = isPreview;
        if (isPreview) {
            this.rawDescription = "Gain [E]. NL Add a *Sparking *Things+1 to your deck. NL Fleeting.";
        } else {
            sparkingThings = new SparkingThings(true);
            this.sparkingThings.upgrade();
            cardsToPreview = sparkingThings;
            this.rawDescription = "Gain [E]. NL Add a *Sparking *Things+1 to your deck. NL Fleeting.";
        }
        initializeDescription();
    }

    public FuelReserve() {
        this(false); // Delegate to the boolean constructor
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        if (isPreview) {
            return;
        }
        // Gain energy
        addToBot(new GainEnergyAction(this.magicNumber));
        // Add upgraded Sparking Things+ to the deck
        SparkingThings addedSparkingThings = new SparkingThings();
        for (int i = 0; i < Math.min(timesUpgraded + 1, 3); i++) {
            addedSparkingThings.upgrade();
        }
        addToBot(new AddCardToDeckAction(addedSparkingThings));
    }

    @Override
    public boolean canUpgrade() {
        return timesUpgraded < 3;
    }

    @Override
    public void upgrade() {
        this.upgraded = true;
        ++this.timesUpgraded;

        int sparkingThingsUpgrades = timesUpgraded;
        String energy = "[E]";
        if (timesUpgraded == 3) {
            upgradeMagicNumber(UPGRADE_ENERGY);
            energy = "[E] [E]";
        } else {
            if (!isPreview && sparkingThings != null) {
                sparkingThings.upgrade();
            }
            sparkingThingsUpgrades++;
        }


        this.name = cardStrings.NAME + "+" + this.timesUpgraded;
        this.rawDescription = "Gain " + energy + ". NL Add a *Sparking *Things+" + sparkingThingsUpgrades + " to your deck. NL Fleeting.";
        initializeDescription();
    }


    @Override
    public AbstractCard makeCopy() {
        return new FuelReserve();
    }

    public final String highlightCard = "SparkingThings+";
    public static final Color goldColor = new Color(-272084481);
}
