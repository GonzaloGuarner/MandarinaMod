package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class Blur extends BaseCard {
    public static final String ID = MandarinaMod.makeID(Blur.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color
            CardType.SKILL,            // Card type
            CardRarity.UNCOMMON,       // Card rarity
            CardTarget.SELF,           // Card target
            1                          // Card cost
    );

    private static final int BLOCK = 5;            // Base block value
    private static final int UPGRADE_PLUS_BLOCK = 3; // Block upgrade

    public Blur() {
        super(ID, info);
        setBlock(BLOCK, UPGRADE_PLUS_BLOCK);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Gain block
        addToBot(new GainBlockAction(p, p, this.block));

        // Apply Blur power
        addToBot(new ApplyPowerAction(p, p, new BlurPower(p, 1), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Blur();
    }
}
