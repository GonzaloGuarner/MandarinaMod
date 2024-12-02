package mandarinamod.cards.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;
import mandarinamod.util.ColorUtils;
import mandarinamod.util.CustomTags;

public class BurdenCycle extends BaseCard {
    public static final String ID = MandarinaMod.makeID(BurdenCycle.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1  // Base cost of 1, upgrades to 0
    );

    public BurdenCycle() {
        super(ID, info);
        initializeDescription();
        tags.add(CustomTags.ODD);
        tags.add(CustomTags.EVEN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Get the leftmost enemy, if there is one
        AbstractMonster leftmostEnemy = getLeftmostEnemy();

        if (leftmostEnemy != null) {
            if (CardUtils.isOddPosition() || CardUtils.isPerfectPosition()) {
                // Odd: Gain 1 Energy for each debuff on the leftmost enemy
                int debuffCount = countDebuffs(leftmostEnemy);
                if (debuffCount > 0) {
                    this.addToBot(new GainEnergyAction(debuffCount));
                }
            }
            if (CardUtils.isEvenPosition() || CardUtils.isPerfectPosition()) {
                AbstractPower leftmostDebuff = getLeftmostDebuff(p);
                if (leftmostDebuff != null) {
                    this.addToBot(new RemoveSpecificPowerAction(p, p, leftmostDebuff.ID));
                }
            }
        }
    }

    private AbstractMonster getLeftmostEnemy() {
        // Returns the leftmost enemy on the screen
        return AbstractDungeon.getMonsters().getRandomMonster(true);
    }

    private int countDebuffs(AbstractMonster m) {
        // Counts the number of debuffs on a specific monster
        return (int) m.powers.stream().filter(power -> power.type == AbstractPower.PowerType.DEBUFF).count();
    }

    private AbstractPower getLeftmostDebuff(AbstractPlayer p) {
        // Returns the leftmost debuff power on the player, if any
        return p.powers.stream()
                .filter(power -> power.type == AbstractPower.PowerType.DEBUFF)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0); // Reduce cost to 0 upon upgrade
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BurdenCycle();
    }


    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        //Default color
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

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

