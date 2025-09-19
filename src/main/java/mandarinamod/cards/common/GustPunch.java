package mandarinamod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.cards.BaseCard;
import mandarinamod.powers.FlowPower;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import com.megacrit.cardcrawl.powers.WeakPower;

public class GustPunch extends BaseCard {
    public static final String ID = makeID(GustPunch.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Specific card color for Mandarina
            CardType.ATTACK,           // Attack card
            CardRarity.COMMON,         // Common rarity
            CardTarget.ENEMY,          // Targets a single enemy
            1                          // Energy cost
    );

    private static final int DAMAGE = 7;             // Base damage
    private static final int UPGRADE_DAMAGE = 3;     // Increase damage by 2 when upgraded
    private static final int WEAK = 1;               // Amount of Weak applied
    private static final int FLOW = 1;               // Amount of Flow gained

    public GustPunch() {
        super(ID, info);
        setDamage(DAMAGE, UPGRADE_DAMAGE);
        setMagic(WEAK);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal damage
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));

        // Apply Weak to the enemy
        addToBot(new ApplyPowerAction(
                m, p, new WeakPower(m, magicNumber, false), magicNumber,
                AbstractGameAction.AttackEffect.NONE));

        if(upgraded){
            // Apply Flow to the player
            addToBot(new ApplyPowerAction(
                    p, p, new FlowPower(p, FLOW), FLOW));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GustPunch();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);  // Increase damage when upgraded
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
