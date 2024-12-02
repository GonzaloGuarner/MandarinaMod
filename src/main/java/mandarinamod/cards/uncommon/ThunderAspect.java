package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbActivateEffect;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.powers.ThunderAspectPower;

public class ThunderAspect extends BaseCard {
    public static final String ID = makeID(ThunderAspect.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.POWER, // The card is a Power type
            CardRarity.UNCOMMON, // Uncommon card rarity
            CardTarget.SELF, // Targets the player
            1 // Costs 1 energy
    );

    // Base AoE damage and its upgrade amount
    private static final int DAMAGE = 3;
    private static final int UPGRADE_DAMAGE = 2;

    public ThunderAspect() {
        super(ID, info);
        // Set the magic number to be used for Lightning Power damage
        setMagic(DAMAGE, UPGRADE_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Apply the Lightning Power to the player
        addToBot(new ApplyPowerAction(p, p, new ThunderAspectPower(p, magicNumber), magicNumber));

        addToBot(new VFXAction(new LightningOrbActivateEffect(p.hb.cX, p.hb.cY), 0.2F));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ThunderAspect();
    }
}
