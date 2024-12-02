package mandarinamod.cards.rare;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.VoidStalkerPower;
import mandarinamod.util.CardStats;


public class VoidStalker extends BaseCard {
    public static final String ID = makeID(VoidStalker.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.POWER, // This is a power card
            CardRarity.RARE, // Card rarity is rare
            CardTarget.SELF, // Targets the player
            3 // Costs 3 energy
    );

    public VoidStalker() {
        super(ID, info);
        setEthereal(true); // Make the card ethereal
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Apply the Void Stalker power to the player
        addToBot(new ApplyPowerAction(p, p, new VoidStalkerPower(p, 3), 3)); // Power amount set to 3 turns
        addToBot(new VFXAction(new DarkOrbActivateEffect(p.hb.cX, p.hb.cY)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new VoidStalker();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            setEthereal(false); // Remove ethereal when upgraded
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
