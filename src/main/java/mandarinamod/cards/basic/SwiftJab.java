package mandarinamod.cards.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;
import mandarinamod.util.ColorUtils;
import mandarinamod.util.CustomTags;

public class SwiftJab extends BaseCard {
    public static final String ID = makeID(SwiftJab.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.ATTACK,           // Card type
            CardRarity.BASIC,         // Common rarity
            CardTarget.ENEMY,          // Targets a single enemy
            0                          // Costs 0 energy
    );

    private static final int DAMAGE = 3;             // Base damage (adjust to match SparkJab)
    private static final int UPGRADE_DAMAGE = 2;     // Increase damage by 2 when upgraded

    public SwiftJab() {
        super(ID, info);
        setDamage(DAMAGE, UPGRADE_DAMAGE);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal damage to the target enemy
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        if(CardUtils.isEvenPosition() || CardUtils.isPerfectPosition()){
            if (!upgraded) {
                // Unupgraded: Discard 1 card, then draw 1 card
                addToBot(new DiscardAction(p, p, 1, false));
                addToBot(new DrawCardAction(p, 1));
            } else {
                // Upgraded: Draw 1 card, then discard 1 card
                addToBot(new DrawCardAction(p, 1));
                addToBot(new DiscardAction(p, p, 1, false));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SwiftJab();
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

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        //Default color
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

        // Odd amount of cards played before
        if (CardUtils.isOddPosition()) {
            // Set the glow color to gold if card would be even
            this.glowColor = ColorUtils.MAGENTA_GLOW.cpy();
        }
        if (CardUtils.isPerfectPosition()) {
            // Set the glow color to gold
            this.glowColor = GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }
}
