package mandarinamod.cards.common;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
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
import mandarinamod.util.CardStats;

public class MidfightFire extends BaseCard {
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

    private static final int BLOCK = 11;            // Base block value
    private static final int UPGRADE_BLOCK = 2;     // Upgrade block value increment
    private static final int SPARKS = 1;            // Number of sparks to add to hand
    private static final int UPGRADE_SPARKS = 1;    // Additional sparks to add to hand on upgrade

    public MidfightFire() {
        super(ID, info);
        setBlock(BLOCK, UPGRADE_BLOCK);
        setMagic(SPARKS, UPGRADE_SPARKS);
        cardsToPreview = new Spark();
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Gain block
        addToBot(new GainBlockAction(p, p, this.block));

        // Add sparks to the player's hand
        AbstractCard spark = new Spark();
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new MakeTempCardInHandAction(spark));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new MidfightFire();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);              // Upgrade block
            upgradeMagicNumber(UPGRADE_SPARKS);       // Upgrade number of Sparks added
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
