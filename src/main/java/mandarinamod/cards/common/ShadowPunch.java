package mandarinamod.cards.common;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;
import mandarinamod.util.ColorUtils;

public class ShadowPunch extends BaseCard implements BranchingUpgradesCard {
    public static final String ID = makeID(ShadowPunch.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Specific card color for Mandarina
            CardType.ATTACK, // Attack card
            CardRarity.COMMON, // Common rarity
            CardTarget.ENEMY, // Targets a single enemy
            1 // Energy cost
    );

    private static final int DAMAGE = 7; // Base damage, increased to match Sucker Punch
    private static final int UPGRADE_DAMAGE = 3; // Increase damage by 2 when upgraded
    private static final int VULNERABLE = 1; // Amount of Vulnerable applied
    private static final int UPGRADE_VULNERABLE = 1; // Additional Vulnerable applied when upgraded

    private boolean upgradeBranchOne = true;

    public ShadowPunch() {
        super(ID, info);
        setDamage(DAMAGE, UPGRADE_DAMAGE);
        setMagic(VULNERABLE, UPGRADE_VULNERABLE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal damage
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));

        // Apply Vulnerable to the enemy
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber,
                AbstractGameAction.AttackEffect.NONE));

        if(!upgradeBranchOne && (CardUtils.isEvenPosition() || CardUtils.isPerfectPosition())){
            this.shuffleBackIntoDrawPile = true;
        }else{
            this.shuffleBackIntoDrawPile = false;
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            super.upgrade();
            //upgradeName();
            if (isBranchUpgrade()) {
                branchUpgrade(); // Upgrade Path 1
            } else {
                baseUpgrade(); // Upgrade Path 2
            }
        }
    }

    public void baseUpgrade() {
        upgradeMagicNumber(UPGRADE_VULNERABLE);
        this.rawDescription = cardStrings.DESCRIPTION;
        upgradeBranchOne = true;
        initializeDescription();

    }


    public void branchUpgrade() {
        upgradeDamage(UPGRADE_DAMAGE);
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        upgradeBranchOne = false;
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShadowPunch();
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        //Default color
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

        if(!upgradeBranchOne){
            if (CardUtils.isOddPosition()) {
                // Set the glow color to magenta if card would be even
                this.glowColor = ColorUtils.MAGENTA_GLOW.cpy();
            }
            if (CardUtils.isPerfectPosition()) {
                // Set the glow color to gold
                this.glowColor = ColorUtils.GOLD_GLOW.cpy();
            }
        }
    }
}
