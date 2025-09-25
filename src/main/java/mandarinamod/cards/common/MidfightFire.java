package mandarinamod.cards.common;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.cards.tempcards.Spark;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.ElectrifyingBarrierPower;
import mandarinamod.powers.FirePlacePower;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;
import mandarinamod.util.ColorUtils;

public class MidfightFire extends BaseCard implements BranchingUpgradesCard {
    public static final String ID = MandarinaMod.makeID(MidfightFire.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Load card strings
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.SKILL,           // This is a skill card
            CardRarity.COMMON,        // Card rarity
            CardTarget.SELF,          // Targets the player
            2                         // Costs 2 energy
    );

    private static final int BLOCK = 11;
    private static final int UPGRADE_BLOCK = 2;
    private static final int SPARKS = 1;            // Number of sparks to add to hand
    private static final int UPGRADE_SPARKS = 1;

    private boolean upgradeBranchOne = true;

    public MidfightFire() {
        super(ID, info);
        setBlock(BLOCK);
        setMagic(SPARKS);
        cardsToPreview = new Spark();
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Gain block
        addToBot(new GainBlockAction(p, p, this.block));

        if(upgradeBranchOne){
            AbstractCard spark = new Spark();
            for (int i = 0; i < magicNumber; i++) {
                addToBot(new MakeTempCardInHandAction(spark));
            }
        }else {
            this.addToBot(new ArmamentsAction(true));
        }
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            super.upgrade();
            if (isBranchUpgrade()) {
                branchUpgrade();
            } else {
                baseUpgrade();
            }
        }
    }

    public void baseUpgrade() {
        upgradeBlock(UPGRADE_BLOCK);
        upgradeMagicNumber(UPGRADE_SPARKS);
        this.rawDescription = cardStrings.DESCRIPTION;
        upgradeBranchOne = true;
        initializeDescription();

    }


    public void branchUpgrade() {
        upgradeBlock(UPGRADE_BLOCK);
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        cardsToPreview = null;
        upgradeBranchOne = false;
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new MidfightFire();
    }
}
