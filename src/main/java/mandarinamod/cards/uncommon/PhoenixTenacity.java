package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.PhoenixTenacityPower;
import mandarinamod.util.CardStats;

public class PhoenixTenacity extends BaseCard {
    public static final String ID = MandarinaMod.makeID(PhoenixTenacity.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.POWER,            // Card type
            CardRarity.UNCOMMON,       // Card rarity
            CardTarget.SELF,           // Card target
            2                          // Card cost
    );

    private static final int BASE_BURNT = 5;           // Base extra card draw amount
    private static final int BURNT_UPGRADE = 3;

    public PhoenixTenacity() {
        super(ID, info);
        setMagic(BASE_BURNT, BURNT_UPGRADE);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Apply Infernal Tenacity power to the player
        addToBot(new ApplyPowerAction(p, p, new PhoenixTenacityPower(p, this.magicNumber), this.magicNumber));
    }


    @Override
    public AbstractCard makeCopy() {
        return new PhoenixTenacity();
    }
}