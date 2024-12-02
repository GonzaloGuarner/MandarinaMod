package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.cards.tempcards.Spark;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.KindleSpiritPower;
import mandarinamod.util.CardStats;

public class KindleSpirit extends BaseCard {
    public static final String ID = MandarinaMod.makeID(KindleSpirit.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.POWER,            // Card type
            CardRarity.UNCOMMON,       // Card rarity
            CardTarget.SELF,           // Card target
            1                          // Card cost
    );

    public KindleSpirit() {
        super(ID, info);
        cardsToPreview = new Spark();
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Apply Kindle Spirit power to the player
        addToBot(new ApplyPowerAction(p, p, new KindleSpiritPower(p, 1), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeBaseCost(0);  // Set cost to 0 after upgrade
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new KindleSpirit();
    }
}