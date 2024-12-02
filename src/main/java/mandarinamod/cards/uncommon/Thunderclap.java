package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.ThunderClapped;
import mandarinamod.util.CardStats;

public class Thunderclap extends BaseCard {
    public static final String ID = MandarinaMod.makeID(Thunderclap.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1 // Card cost
    );

    private static final int DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 3;

    public Thunderclap() {
        super(ID, info);
        setDamage(DAMAGE);
        initializeDescription();
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal damage to the target
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));

        // Apply the "Marked for Thunder" Power
        addToBot(new ApplyPowerAction(m, p, new ThunderClapped(m, p)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Thunderclap();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE ); // Increase damage
            initializeDescription();
        }
    }
}
