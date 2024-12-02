package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.TransformCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import mandarinamod.MandarinaMod;
import mandarinamod.actions.WindOfChangeAction;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class WindOfChange extends BaseCard {
    public static final String ID = MandarinaMod.makeID(WindOfChange.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.SKILL,            // Card type
            CardRarity.UNCOMMON,           // Rarity
            CardTarget.SELF,           // Target is self
            1                          // Cost is 1 energy
    );

    public WindOfChange() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = 3; // Transform up to 3 cards
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new WindOfChangeAction(this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            setSelfRetain(true); // Gains Retain when upgraded
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WindOfChange();
    }
}
