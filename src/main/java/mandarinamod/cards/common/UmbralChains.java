package mandarinamod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class UmbralChains extends BaseCard {
    public static final String ID = MandarinaMod.makeID(UmbralChains.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.ATTACK,            // Card type
            CardRarity.COMMON,          // Card rarity
            CardTarget.ENEMY,           // Targets a single enemy
            2                           // Cost
    );

    private static final int DAMAGE = 12;


    private static final int STRENGTH_LOSS = 5;
    private static final int UPGRADE_STRENGTH_LOSS = 4; // Total strength loss when upgraded is 9 (5 + 4)

    public UmbralChains() {
        super(ID, info);
        setDamage(DAMAGE);
        setMagic(STRENGTH_LOSS, UPGRADE_STRENGTH_LOSS); // magicNumber represents strength loss
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal damage
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

        this.addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -this.magicNumber), -this.magicNumber));
        if (m != null && !m.hasPower("Artifact")) {
            this.addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new UmbralChains();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();// Increase damage
            upgradeMagicNumber(UPGRADE_STRENGTH_LOSS); // Increase strength loss
            initializeDescription();
        }
    }
}
