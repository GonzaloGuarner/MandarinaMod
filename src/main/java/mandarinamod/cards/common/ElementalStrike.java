package mandarinamod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class ElementalStrike extends BaseCard {
    public static final String ID = makeID(ElementalStrike.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.ATTACK, // This is an attack card
            CardRarity.COMMON, // Card rarity
            CardTarget.ENEMY, // Targets a single enemy
            2 // Costs 2 energy
    );

    // Setting base damage and upgrade values
    private static final int DAMAGE = 13;
    private static final int DAMAGE_UPGRADE = 1;
    private static final int DEBUFF_AMOUNT = 1;
    private static final int DEBUFF_UPGRADE = 2;

    public ElementalStrike() {
        super(ID, info);

        // Use BaseCard's set methods to set damage and upgrade values
        setDamage(DAMAGE, DAMAGE_UPGRADE);
        setMagic(DEBUFF_AMOUNT, DEBUFF_UPGRADE); // Use magicNumber to store debuff application value
        tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Damage the enemy
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

        // If the enemy has any debuff, apply Vulnerable and Weak
        if (m.powers.stream().anyMatch(power -> power.type == AbstractPower.PowerType.DEBUFF)) {
            addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber));
            addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            // Upgrade the card's name and apply damage and magic upgrades
            upgradeName();
            upgradeDamage(DAMAGE_UPGRADE);
            upgradeMagicNumber(DEBUFF_UPGRADE);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ElementalStrike();
    }
}
