package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;
import mandarinamod.util.ColorUtils;
import mandarinamod.util.CustomTags;

public class ShadowRitual extends BaseCard {
    public static final String ID = MandarinaMod.makeID(ShadowRitual.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Load card strings
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String SECOND_STAGE_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[0];
    public static final String FINAL_STAGE_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[1];
    public static final String FINAL_STAGE_UPGRADED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[2];

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.SKILL,            // Card type
            CardRarity.UNCOMMON,       // Rarity
            CardTarget.SELF,           // Targets self
            1                          // Costs 1 energy
    );

    private static final int BLOCK = 5;
    private static final int UPGRADE_BLOCK = 1;
    private static final int WEAK_DURATION = 1;
    private static final int VULNERABLE_DURATION = 1;
    private static final int UPGRADE_VULNERABLE = 2;
    private static final int DRAW_CARDS = 2;
    private static final int UPGRADE_DRAW = 3;
    private static final int ENERGY_GAIN = 3;
    private static final int UPGRADE_ENERGY = 4;

    private enum RitualStage {
        FIRST, SECOND, THIRD
    }

    private RitualStage currentStage = RitualStage.FIRST;

    public ShadowRitual() {
        super(ID, info);
        setBlock(BLOCK, UPGRADE_BLOCK);
        initializeDescription();
        tags.add(CustomTags.EVEN);
        tags.add(CustomTags.ODD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        switch (currentStage) {
            case FIRST:
                // Gain Block
                addToBot(new GainBlockAction(p, p, this.block));
                if (CardUtils.isEvenPosition() || CardUtils.isPerfectPosition()) {
                    // Move to next stage
                    currentStage = RitualStage.SECOND;
                    // Update description to next stage
                    this.rawDescription = SECOND_STAGE_DESCRIPTION;
                    initializeDescription();
                }
                break;
            case SECOND:
                // Apply Weak to all enemies
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    addToBot(new ApplyPowerAction(monster, p, new WeakPower(monster, WEAK_DURATION, false), WEAK_DURATION, true, AbstractGameAction.AttackEffect.NONE));
                }
                if (CardUtils.isOddPosition() || CardUtils.isPerfectPosition()) {
                    // Move to final stage
                    currentStage = RitualStage.THIRD;
                    // Update description to final stage based on upgrade status
                    int vulnerable = upgraded ? UPGRADE_VULNERABLE : VULNERABLE_DURATION;
                    int drawAmount = upgraded ? UPGRADE_DRAW : DRAW_CARDS;
                    int energyGain = upgraded ? UPGRADE_ENERGY : ENERGY_GAIN;
                    this.rawDescription = "Apply " + vulnerable + " Vulnerable to ALL enemies. NL If this is the 3rd card played this turn, draw " + drawAmount + " cards, gain " + energyGain + " [E] and Exhaust.";

                    initializeDescription();

                }
                break;
            case THIRD:
                // Apply Vulnerable to all enemies
                int vulnerableDuration = upgraded ? UPGRADE_VULNERABLE : VULNERABLE_DURATION;
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    addToBot(new ApplyPowerAction(monster, p, new VulnerablePower(monster, vulnerableDuration, false), vulnerableDuration, true, AbstractGameAction.AttackEffect.NONE));
                }
                if (CardUtils.isThirdPosition() || CardUtils.isPerfectPosition()) {
                    // Draw Cards
                    int drawAmount = upgraded ? UPGRADE_DRAW : DRAW_CARDS;
                    addToBot(new DrawCardAction(p, drawAmount));
                    // Gain Energy
                    int energyGain = upgraded ? UPGRADE_ENERGY : ENERGY_GAIN;
                    addToBot(new GainEnergyAction(energyGain));

                    // Reset to the initial stage
                    currentStage = RitualStage.FIRST;
                    // Reset description to the initial stage
                    this.rawDescription = upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION;

                    setExhaust(true);
                    initializeDescription();
                }
                break;
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
            upgradeRitualDescription();
            initializeDescription();
        }
    }
    void upgradeRitualDescription(){
        switch (currentStage) {
            case FIRST:
                this.rawDescription = UPGRADE_DESCRIPTION;
                break;
            case SECOND:
                this.rawDescription = SECOND_STAGE_DESCRIPTION;
                break;
            case THIRD:
                this.rawDescription = "Apply " + UPGRADE_VULNERABLE +
                        " Vulnerable to ALL enemies. ${modID}:Third: Draw " +
                        UPGRADE_DRAW + " cards, gain " + UPGRADE_ENERGY + " [E] and exhaust this card. NL Exhaust.";
                break;
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        //Default color
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

        switch (currentStage) {
            case FIRST:
                // Odd amount of cards played before
                if (CardUtils.isOddPosition()) {
                    // Set the glow color to magenta if card would be even
                    this.glowColor = ColorUtils.MAGENTA_GLOW.cpy();

                }
                if (CardUtils.isPerfectPosition()) {
                    // Set the glow color to gold
                    this.glowColor = ColorUtils.GOLD_GLOW.cpy();
                }
                break;
            case SECOND:
                if (CardUtils.isEvenPosition()) {
                    // Set the glow color to green if card would be odd
                    this.glowColor = ColorUtils.GREEN_GLOW.cpy();
                }
                if (CardUtils.isPerfectPosition()) {
                    // Set the glow color to gold
                    this.glowColor = ColorUtils.GOLD_GLOW.cpy();
                }
                break;
            case THIRD:
                if (CardUtils.isNPosition(2) || CardUtils.isPerfectPosition()) {
                    this.glowColor = ColorUtils.GOLD_GLOW.cpy();
                }

                break;
        }
    }
    @Override
    public AbstractCard makeCopy() {
        return new ShadowRitual();
    }
}
