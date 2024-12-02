package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class ParalyzingTouch extends BaseCard {
    public static final String ID = makeID(ParalyzingTouch.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,  // Card color for Mandarina
            CardType.ATTACK,            // This is an attack card
            CardRarity.UNCOMMON,        // Uncommon rarity
            CardTarget.ENEMY,           // Targets a single enemy
            1                           // Costs 1 energy
    );

    // Load card strings
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int DAMAGE = 9;
    private static final int UPGRADE_DAMAGE = 3;     // Increases damage by 3 when upgraded
    private static final int STRENGTH_LOSS = 1;
    private static final int UPGRADE_STRENGTH_LOSS = 1;  // Increases Strength reduction by 1 when upgraded

    public ParalyzingTouch() {
        super(ID, info);
        setDamage(DAMAGE, UPGRADE_DAMAGE);
        setMagic(STRENGTH_LOSS, UPGRADE_STRENGTH_LOSS);
        setExhaust(true);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal damage to the target enemy
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.LIGHTNING));

        // Reduce the enemy's Strength by magicNumber
        addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -this.magicNumber), -this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);              // Upgrade damage
            upgradeMagicNumber(UPGRADE_STRENGTH_LOSS);  // Upgrade Strength reduction
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ParalyzingTouch();
    }
}
