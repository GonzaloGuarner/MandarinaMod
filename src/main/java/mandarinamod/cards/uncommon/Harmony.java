package mandarinamod.cards.uncommon;

import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.cards.tempcards.Spark;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.CursedBlock;
import mandarinamod.powers.FlowPower;
import mandarinamod.powers.StaticPower;
import mandarinamod.util.CardStats;

public class Harmony extends BaseCard implements BranchingUpgradesCard {
    public static final String ID = MandarinaMod.makeID(Harmony.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,    // Card color (adjust if needed)
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1                   // Cost
    );
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String BRANCH_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[0];

    private static final int FLOW = 2;
    private static final int STATIC_CHARGE = 4;
    private static final int CURSED_BLOCK = 4;
    private static final int SPARK_AMOUNT = 1;

    private static final int FLOW_UPGRADE_1 = 1;
    private static final int STATIC_CHARGE_UPGRADE_1 = 1;
    private static final int CURSED_BLOCK_UPGRADE_1 = 2;

    private static final int FLOW_UPGRADE_2 = 2;
    private static final int STATIC_CHARGE_UPGRADE_2 = 5;
    private static final int CURSED_BLOCK_UPGRADE_2 = 6;
    private static final int SPARKS_UPGRADE_2 = 1;

    private int staticChargeAmount = 1;
    private AbstractCard spark = new Spark();
    private int sparkAmount = 0;


    public Harmony() {
        super(ID, info);
        setMagic(FLOW);
        staticChargeAmount = STATIC_CHARGE;
        spark = new Spark();
        sparkAmount = SPARK_AMOUNT;
        setBlock(CURSED_BLOCK);
        cardsToPreview = spark;

        BlockModifierManager.addModifier(this, new CursedBlock());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        // Apply Flow
        addToBot(new ApplyPowerAction(player, player, new FlowPower(player, this.magicNumber), this.magicNumber));

        // Apply Static Charge
        addToBot(new ApplyPowerAction(player, player, new StaticPower(player, staticChargeAmount), staticChargeAmount));

        // Add Sparks
        addToBot(new MakeTempCardInHandAction(spark, sparkAmount));

        // Apply Cursed Block
        addToBot(new GainCustomBlockAction(this, player, this.block));

        // Play sound effect (optional for thematic flair)
//        addToBot(new SFXAction("THUNDER_CLAP"));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            if (isBranchUpgrade()) {
                branchUpgrade();
            } else {
                baseUpgrade();
            }
        }
    }

    public void baseUpgrade() {

        // Upgrade 1: Increases values, retains cost 1
        upgradeMagicNumber(FLOW_UPGRADE_1);
        staticChargeAmount+=STATIC_CHARGE_UPGRADE_1;
        spark.upgrade();
        upgradeBlock(CURSED_BLOCK_UPGRADE_1);
        cardsToPreview = spark;
        this.rawDescription = UPGRADE_DESCRIPTION;
        initializeDescription();
    }

    public void branchUpgrade() {
        // Upgrade 2: Costs 2, significantly boosts values
        this.cost = this.costForTurn = 2;
        upgradeMagicNumber(FLOW_UPGRADE_2);
        staticChargeAmount+=STATIC_CHARGE_UPGRADE_2;
        sparkAmount+=SPARKS_UPGRADE_2;
        upgradeBlock(CURSED_BLOCK_UPGRADE_2);

        this.rawDescription = BRANCH_DESCRIPTION;
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new Harmony();
    }
}
