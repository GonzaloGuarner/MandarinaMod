package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.StaticPower;
import mandarinamod.util.CardStats;

public class StaticCharge extends BaseCard {
    public static final String ID = MandarinaMod.makeID(StaticCharge.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Load card strings
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1 // Costs 1 energy
    );

    private static final int BLOCK = 6;                  // Amount of Block gained
    private static final int UPGRADE_PLUS_BLOCK = 0;     // Upgrade: Additional block
    private static final int THORNS_DAMAGE = 7;          // Damage dealt when attacked
    private static final int UPGRADE_PLUS_THORNS_DAMAGE = 5; // Upgrade: Additional damage when upgraded

    public StaticCharge() {
        super(ID, info);
        setBlock(BLOCK, UPGRADE_PLUS_BLOCK);
        setMagic(THORNS_DAMAGE, UPGRADE_PLUS_THORNS_DAMAGE);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Gain block
        addToBot(new GainBlockAction(p, p, this.block));

        // Apply Static Charge power to the player
        addToBot(new ApplyPowerAction(p, p, new StaticPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new StaticCharge();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);           // Upgrade block amount
            upgradeMagicNumber(UPGRADE_PLUS_THORNS_DAMAGE); // Upgrade counter damage amount
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
