package mandarinamod.cards.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.FlowPower;
import mandarinamod.util.CardStats;

public class EbbAndFlow extends BaseCard {
    public static final String ID = MandarinaMod.makeID(EbbAndFlow.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.SKILL,            // This is a skill card
            CardRarity.COMMON,         // Common rarity
            CardTarget.SELF,           // Targets self
            1                          // Costs 1 energy
    );

    // Load card strings
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int BLOCK = 6;
    private static final int UPGRADE_BLOCK = 2;
    private static final int FLOW_AMOUNT = 2;
    private static final int UPGRADE_FLOW = 1;

    public EbbAndFlow() {
        super(ID, info);
        setBlock(BLOCK, UPGRADE_BLOCK);
        setMagic(FLOW_AMOUNT, UPGRADE_FLOW);
        this.rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Gain Block
        addToBot(new GainBlockAction(p, p, this.block));
        // Gain Flow
        addToBot(new ApplyPowerAction(p, p, new FlowPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);        // Increase Block by 2 when upgraded
            upgradeMagicNumber(UPGRADE_FLOW);   // Increase Flow gain by 1 when upgraded
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new EbbAndFlow();
    }
}
