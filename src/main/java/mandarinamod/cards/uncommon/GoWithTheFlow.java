package mandarinamod.cards.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.powers.FlowPower;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import com.megacrit.cardcrawl.vfx.combat.AdrenalineEffect;
import com.megacrit.cardcrawl.vfx.combat.SanctityEffect;
import mandarinamod.util.ColorUtils;
import mandarinamod.util.CustomTags;

public class GoWithTheFlow extends BaseCard {
    public static final String ID = makeID(GoWithTheFlow.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Load card strings
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color
            CardType.SKILL,            // Card type
            CardRarity.UNCOMMON,       // Rarity
            CardTarget.SELF,           // Targets self
            0                          // Costs 0 energy
    );

    private static final int FLOW_OR_ENERGY = 1;          // Base Flow or Energy gained
    private static final int UPGRADE_PLUS_FLOW_OR_ENERGY = 1;  // Upgrade: Increase Flow or Energy gained by 1

    public GoWithTheFlow() {
        super(ID, info);
        setMagic(FLOW_OR_ENERGY, UPGRADE_PLUS_FLOW_OR_ENERGY);
        this.rawDescription = DESCRIPTION;
        initializeDescription();
        tags.add(CustomTags.ODD);
        tags.add(CustomTags.EVEN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (CardUtils.isOddPosition() || CardUtils.isPerfectPosition()) {
            // Odd: Draw 2 cards. Gain magicNumber Flow.
            addToBot(new VFXAction(p, new SanctityEffect(p.hb.cX, p.hb.cY), 0.1F));
            addToBot(new DrawCardAction(1));
            addToBot(new ApplyPowerAction(p, p, new FlowPower(p, magicNumber), magicNumber));
        }
        if(CardUtils.isEvenPosition() || CardUtils.isPerfectPosition()){
            // Even: Discard 2 cards. Gain magicNumber Energy.
            addToBot(new VFXAction(p, new AdrenalineEffect(), 0.1F));
            addToBot(new DiscardAction(p, p, 2, false));
            addToBot(new GainEnergyAction(magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GoWithTheFlow();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_FLOW_OR_ENERGY); // Increase magicNumber by 1
            this.rawDescription = UPGRADE_DESCRIPTION;
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
            // Set the glow color to magenta if card would be even
            this.glowColor = ColorUtils.MAGENTA_GLOW.cpy();
        }
        if (CardUtils.isEvenPosition()) {
            // Set the glow color to green if card would be odd
            this.glowColor = ColorUtils.GREEN_GLOW.cpy();
        }
        if (CardUtils.isPerfectPosition()) {
            // Set the glow color to gold
            this.glowColor = ColorUtils.GOLD_GLOW.cpy();
        }
    }
}
