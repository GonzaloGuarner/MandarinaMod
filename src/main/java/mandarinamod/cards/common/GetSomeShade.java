package mandarinamod.cards.common;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class GetSomeShade extends BaseCard {
    public static final String ID = MandarinaMod.makeID(GetSomeShade.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Initial block and block increase value
    private static final int BASE_BLOCK = 3;
    private static final int UPGRADE_BLOCK = 2;
    private static final int BLOCK_INCREMENT = 1;

    public GetSomeShade() {
        super(ID, new CardStats(
                Mandarina.Meta.CARD_COLOR, // Card color
                CardType.SKILL,            // Type: Skill
                CardRarity.COMMON,          // Rarity: Common
                CardTarget.SELF,            // Targets: Self
                0                           // Cost: 0
        ));

        // Set the initial block values
        this.baseBlock = BASE_BLOCK;
        this.magicNumber = this.baseMagicNumber = BLOCK_INCREMENT; // The block increment
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Gain block
        addToBot(new GainBlockAction(p, this.block));

        // Increase the block of this card by 1 for the rest of the combat
        this.baseBlock += this.magicNumber;
        this.applyPowers(); // Update the block shown
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            // Increase the base block for the upgraded version
            upgradeBlock(UPGRADE_BLOCK); // 5 instead of 3
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GetSomeShade();
    }
}
