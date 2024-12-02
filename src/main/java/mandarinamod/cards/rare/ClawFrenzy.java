package mandarinamod.cards.rare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.actions.ClawFrenzyAction;
import mandarinamod.util.CustomTags;
import mandarinamod.vfx.combat.ClawEffect;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;

public class ClawFrenzy extends BaseCard {
    public static final String ID = MandarinaMod.makeID(ClawFrenzy.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Load card strings
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color
            CardType.ATTACK,           // Card type
            CardRarity.RARE,           // Rarity
            CardTarget.ENEMY,          // Target
            -1                         // Cost X
    );

    private static final int DAMAGE = 8;              // Damage per hit
    private static final int UPGRADE_PLUS_DAMAGE = 0; // No damage increase when upgraded
    private static final int VULNERABLE = 2;          // Vulnerable duration
    private static final int UPGRADE_MINUS_VULNERABLE = 1;  // Reduce Vulnerable by 1 when upgraded

    public ClawFrenzy() {
        super(ID, info);
        setDamage(DAMAGE, UPGRADE_PLUS_DAMAGE);
        setMagic(VULNERABLE, -UPGRADE_MINUS_VULNERABLE);  // magicNumber represents Vulnerable duration
        initializeDescription();
        tags.add(CustomTags.EVEN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Use the custom ClawFrenzyAction
        addToBot(new ClawFrenzyAction(p, m, this.damage, this.damageTypeForTurn, this.freeToPlayOnce, this.energyOnUse));
        // Apply Vulnerable to self
        addToBot(new ApplyPowerAction(p, p,
                new com.megacrit.cardcrawl.powers.VulnerablePower(p, this.magicNumber, false),
                this.magicNumber));
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        //Default color
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

        // Odd amount of cards played before
        if (CardUtils.isOddPosition()) {
            // Set the glow color to green if card would be even
            this.glowColor = GREEN_BORDER_GLOW_COLOR.cpy();
        }
        if (CardUtils.isPerfectPosition()) {
            // Set the glow color to gold
            this.glowColor = GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }
    @Override
    public AbstractCard makeCopy() {
        return new ClawFrenzy();
    }
}
