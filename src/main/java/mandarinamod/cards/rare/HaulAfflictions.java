package mandarinamod.cards.rare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ChokePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import mandarinamod.actions.HaulAfflictionsAction;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HaulAfflictions extends BaseCard {
    public static final String ID = makeID(HaulAfflictions.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Specific card color for Mandarina
            CardType.SKILL,             // This is a skill card
            CardRarity.RARE,            // Rare card rarity
            CardTarget.ALL_ENEMY,       // Targets all enemies
            2                           // Costs 2 energy
    );

    private static final int MULTIPLIER = 6;         // Base damage multiplier per debuff
    private static final int CHOKE_AMOUNT = 3;       // Applies 2 Choke to all enemies
    private static final int CHOKE_UPGRADE_AMOUNT = 4;

    // Load card strings
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    public HaulAfflictions() {
        super(ID, info);
        setMagic(MULTIPLIER);
        isMultiDamage = true; // Indicates that the card deals damage to multiple enemies
        this.rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int chokeAmount = upgraded ? CHOKE_UPGRADE_AMOUNT : CHOKE_AMOUNT;
        addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.SLATE, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.5F));
        // Apply Weak and Choke to all enemies
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            addToBot(new ApplyPowerAction(monster, p, new ChokePower(monster, chokeAmount), chokeAmount, true));
            if(upgraded)
                addToBot(new ApplyPowerAction(monster, p, new WeakPower(monster, 1, false), 1, true));
        }

        // Add custom action to count debuffs and deal damage after Choke is applied
        addToBot(new HaulAfflictionsAction(p, magicNumber, damageTypeForTurn));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new HaulAfflictions();
    }
}
