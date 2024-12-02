package mandarinamod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class DoubleStrike extends BaseCard {
    public static final String ID = makeID(DoubleStrike.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Specific card color for Mandarina
            CardType.ATTACK, // Card type is ATTACK
            CardRarity.COMMON, // Common card rarity
            CardTarget.ENEMY, // Targets a single enemy
            1 // Base energy cost
    );

    // Setting base damage and upgrade values
    private static final int DAMAGE = 5;
    private static final int UPG_DAMAGE = 2;

    public DoubleStrike() {
        super(ID, info);

        // Use BaseCard's set methods to set damage and upgrade values
        setDamage(DAMAGE, UPG_DAMAGE);

        // Adding tags similar to Strike for consistency
        tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Add two DamageAction instances to attack the same enemy twice
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            // Upgrade card's name and apply damage upgrade
            upgradeName();
            upgradeDamage(UPG_DAMAGE); // Upgrade damage per hit by UPG_DAMAGE
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DoubleStrike();
    }
}
