package mandarinamod.cards.unused;

import basemod.patches.com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen.NoCompendium;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;
import mandarinamod.util.ColorUtils;
import mandarinamod.util.CustomTags;

@NoCompendium
public class SparkJab extends BaseCard {
    public static final String ID = makeID(SparkJab.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Specific card color for Mandarina
            CardType.ATTACK, // Attack card
            CardRarity.SPECIAL, // Rarity Uncommon
            CardTarget.ENEMY, // Targets a single enemy
            0 // Energy cost 0
    );

    private static final int DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 2;

    public SparkJab() {
        super(ID, info);
        setDamage(DAMAGE, UPGRADE_DAMAGE);
        tags.add(CustomTags.ODD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal damage
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.NONE));
        addToBot(new VFXAction(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AbstractGameAction.AttackEffect.FIRE), 0.1F));
        // Check if played in an Odd position
        if (CardUtils.isOddPosition() || CardUtils.isPerfectPosition()) {
            // Draw 1 card
            addToBot(new DrawCardAction(1));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            // If upgrading affects other aspects, update them here
            // this.rawDescription = UPGRADE_DESCRIPTION;
            // initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        //Default color
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

        // Even amount of cards played before
        if (CardUtils.isEvenPosition()) {
            // Set the glow color to gold if card would be odd
            this.glowColor = ColorUtils.GREEN_GLOW.cpy();
        }
        if (CardUtils.isPerfectPosition()) {
            // Set the glow color to gold
            this.glowColor = GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SparkJab();
    }
}
