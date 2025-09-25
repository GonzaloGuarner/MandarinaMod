package mandarinamod.cards.uncommon;


import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.MultiUpgradeCard;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.cards.tempcards.FuelReserve;
import mandarinamod.cards.tempcards.Spark;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.vfx.combat.SparkingThingsEffect;

public class SparkingThings extends BaseCard {
    public static final String ID = MandarinaMod.makeID(SparkingThings.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            1
    );

    private static final int SPARKS = 2;
    private static final int UPGRADE_SPARKS = 1;
    private boolean isPreview;
    private AbstractCard previewCard;
    private AbstractCard fuelReserve;

    public SparkingThings(boolean isPreview) {
        super(ID, info);
        setMagic(SPARKS);
        FleetingField.fleeting.set(this, true);
        this.isPreview = isPreview;
        if(isPreview){
            this.rawDescription =  "Add "+ this.magicNumber + " *Sparks to your hand. NL Add a *FuelReserve to your deck. NL Fleeting.";
        }else{
            fuelReserve = new FuelReserve(true);
            //this.sparkingThings.upgrade();
            MultiCardPreview.add(this, new Spark(), fuelReserve);
            this.rawDescription =  "Add "+ this.magicNumber + " *Sparks to your hand. NL Add a *FuelReserve to your deck. NL Fleeting.";
        }
        initializeDescription();
    }
    public SparkingThings() {
        this(false); // Delegate to the boolean constructor
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        if(isPreview){
            return;
        }
        addToBot(new VFXAction(new SparkingThingsEffect(player.hb.cX, player.hb.cY, this.magicNumber)));
        // Add Sparks to hand
        addToBot(new MakeTempCardInHandAction(new Spark(), magicNumber));
        // Add Fuel Reserve to the deck
        FuelReserve addedFuelReserve = new FuelReserve();
        for (int i = 0; i < timesUpgraded; i++) {
            addedFuelReserve.upgrade();
        }
        addToBot(new AddCardToDeckAction(addedFuelReserve));
    }
    @Override
    public boolean canUpgrade(){
        return timesUpgraded<3;
    }

    @Override
    public void upgrade() {
        this.upgraded = true;
        ++this.timesUpgraded;

        if(!isPreview && fuelReserve != null){
            fuelReserve.upgrade();
        }
        upgradeMagicNumber(1);

        this.name = cardStrings.NAME + "+" + this.timesUpgraded;
        this.rawDescription =  "Add "+ this.magicNumber + " *Sparks to your hand. NL Add a Fuel Reserve+"+this.timesUpgraded+" to your deck. NL Fleeting.";
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new SparkingThings();
    }
}
