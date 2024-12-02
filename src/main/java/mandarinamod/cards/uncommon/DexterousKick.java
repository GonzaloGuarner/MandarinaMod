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
import com.megacrit.cardcrawl.powers.DexterityPower;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class DexterousKick extends BaseCard {
    public static final String ID = makeID(DexterousKick.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,  // Specific card color for Mandarina
            CardType.ATTACK,            // This is an attack card
            CardRarity.UNCOMMON,        // Uncommon rarity
            CardTarget.ENEMY,           // Targets a single enemy
            1                           // Costs 1 energy
    );

    // Load card strings
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int DAMAGE = 8;
    private static final int UPGRADE_DAMAGE = 3;    // Increase damage to 11 when upgraded
    private static final int DEXTERITY_GAIN = 1;
    private static final int UPGRADE_DEXTERITY_GAIN = 1;

    public DexterousKick() {
        super(ID, info);
        setDamage(DAMAGE, UPGRADE_DAMAGE);
        setMagic(DEXTERITY_GAIN, UPGRADE_DEXTERITY_GAIN);  // Magic number represents Dexterity gain
        initializeDescription();
        this.exhaust = true; // The card exhausts after use
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal damage to target enemy
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

        // Gain Dexterity
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DexterousKick();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE); // Increase damage by 3
            upgradeMagicNumber(UPGRADE_DEXTERITY_GAIN); // Increase Dexterity gain by 1
            initializeDescription();
        }
    }
}
