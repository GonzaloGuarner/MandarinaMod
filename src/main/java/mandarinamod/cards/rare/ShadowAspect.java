package mandarinamod.cards.rare;

import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModContainer;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.CursedBlock;
import mandarinamod.powers.ShadowAspectPower;
import mandarinamod.vfx.combat.SmokeCloudEffect;
import mandarinamod.util.CardStats;

public class ShadowAspect extends BaseCard {
    public static final String ID = makeID(ShadowAspect.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.POWER,            // Power card type
            CardRarity.RARE,           // Rare rarity
            CardTarget.SELF,           // Targets self
            1                          // Costs 1 energy
    );

    private static final int BLOCK = 2;
    private static final int UPG_BLOCK = 1;
    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 1;

    // Load card strings
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    public ShadowAspect() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);
        setMagic(MAGIC, UPG_MAGIC); // Set magic number and upgrade value
        this.rawDescription = DESCRIPTION;
        initializeDescription();
        BlockModifierManager.addModifier(this, new CursedBlock());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Apply Shadow Aspect power to the player
        addToBot(new ApplyPowerAction(p, p, new ShadowAspectPower(this, p, magicNumber), magicNumber));

        // Add Shadow Effect for a dramatic impact
        addToBot(new VFXAction(p, new SmokeCloudEffect(p.hb.cX, p.hb.cY), 0.2F));
    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK);
            upgradeMagicNumber(UPG_MAGIC); // Properly upgrade the magic number
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShadowAspect();
    }
}
