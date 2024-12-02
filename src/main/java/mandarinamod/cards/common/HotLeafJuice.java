package mandarinamod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class HotLeafJuice extends BaseCard {
    public static final String ID = MandarinaMod.makeID(HotLeafJuice.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Load card strings
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.SKILL,            // Skill card type
            CardRarity.COMMON,       // Rarity: Common
            CardTarget.SELF,           // Targets self
            0                          // Costs 0 energy
    );

    private static final int ENERGY_GAIN = 1;          // Base energy gain
    private static final int UPGRADE_ENERGY_GAIN = 1;  // Upgrade adds +1 energy gain

    public HotLeafJuice() {
        super(ID, info);
        setMagic(ENERGY_GAIN, UPGRADE_ENERGY_GAIN); // Magic number represents energy gain
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Gain energy
        addToBot(new GainEnergyAction(this.magicNumber));

        // Add a Burn card to the draw pile
        AbstractCard burn = new Burn();
        addToBot(new MakeTempCardInDrawPileAction(burn, 1, true, true));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_ENERGY_GAIN); // Increase energy gain by 1
//            this.rawDescription = UPGRADE_DESCRIPTION;
//            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new HotLeafJuice();
    }
}
