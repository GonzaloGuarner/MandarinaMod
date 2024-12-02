package mandarinamod.cards.rare;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.EmpowerEffect;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.FlameAspectPower;
import mandarinamod.powers.GustAspectPower;
import mandarinamod.powers.ShadowAspectPower;
import mandarinamod.powers.ThunderAspectPower;
import mandarinamod.util.CardStats;

public class ElementalSurge extends BaseCard {
    public static final String ID = makeID(ElementalSurge.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Specific card color for Mandarina
            CardType.SKILL,            // This is a skill card
            CardRarity.RARE,           // Rare card rarity
            CardTarget.SELF,           // Targets the player
            2                          // Costs 2 energy
    );

    private static final int BUFF_AMOUNT = 1;         // Base Strength and Dexterity gain
    private static final int UPGRADE_BUFF_AMOUNT = 1; // Upgrade increases gain by 1

    public ElementalSurge() {
        super(ID, info);
        setMagic(BUFF_AMOUNT, UPGRADE_BUFF_AMOUNT);
        this.exhaust = true;  // The card will exhaust when played
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int activeElementalPowers = countElementalPowers(p);

        // Gain Strength and Dexterity equal to the number of active elemental powers
        if (activeElementalPowers > 0) {
            addToBot(new VFXAction(new EmpowerEffect(p.hb.cX, p.hb.cY)));
            int totalBuff = magicNumber * activeElementalPowers;
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, totalBuff), totalBuff));
            addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, totalBuff), totalBuff));
        }
    }

    // Method to count active elemental powers (e.g., Thunder, Flame, Shadow)
    private int countElementalPowers(AbstractPlayer p) {
        int count = 0;
        if (p.hasPower(GustAspectPower.POWER_ID)) count++;
        if (p.hasPower(ThunderAspectPower.POWER_ID)) count++;
        if (p.hasPower(FlameAspectPower.POWER_ID)) count++;
        if (p.hasPower(ShadowAspectPower.POWER_ID)) count++;

        return count;
    }

    @Override
    public AbstractCard makeCopy() {
        return new ElementalSurge();
    }
}
