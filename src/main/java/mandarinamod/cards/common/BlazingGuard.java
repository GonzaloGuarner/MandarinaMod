package mandarinamod.cards.common;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.cards.tempcards.Spark;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.FirePlacePower;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;
import mandarinamod.util.ColorUtils;
import mandarinamod.util.CustomTags;

public class BlazingGuard extends BaseCard implements BranchingUpgradesCard {
    public static final String ID = MandarinaMod.makeID(BlazingGuard.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1 // Cost
    );
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int BLOCK = 8;
    private static final int UPGRADE_BLOCK = 2;
    private static final int UPGRADE_BLOCK_BRANCH = 4;
    private static final int DRAW_AMOUNT = 1;
    private static final int BURNT_NUMBER = 4;

    private boolean upgradeBranchOne = true;

    public BlazingGuard() {
        super(ID, info);
        setBlock(BLOCK);
        setMagic(DRAW_AMOUNT);
        setCustomVar("burntnumber", BURNT_NUMBER);
        this.rawDescription = DESCRIPTION;
        initializeDescription();
        tags.add(CustomTags.EVEN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Gain Block
        addToBot(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.block));

        if (CardUtils.isEvenPosition() || CardUtils.isPerfectPosition()) {
            addToBot(new DrawCardAction(magicNumber, new AbstractGameAction() {
                @Override
                public void update() {
                    if (upgraded && upgradeBranchOne) {
                        for (AbstractCard c : DrawCardAction.drawnCards)
                            addToTop(new UpgradeSpecificCardAction(c));
                    }
                    isDone = true;
                }
            }));
        }
        if(!upgradeBranchOne){
            if(CardUtils.isThirdPosition() || CardUtils.isPerfectPosition()){
                addToBot(new ApplyPowerAction(p, p, new FirePlacePower(p, BURNT_NUMBER), BURNT_NUMBER));
            }
        }
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            super.upgrade();
            if (isBranchUpgrade()) {
                branchUpgrade();
            } else {
                baseUpgrade();
            }
        }
    }

    public void baseUpgrade() {
        upgradeBlock(UPGRADE_BLOCK);
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
        upgradeBranchOne = true;
    }


    public void branchUpgrade() {
        upgradeBlock(UPGRADE_BLOCK_BRANCH);
        upgradeBaseCost(2);
        this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
        upgradeBranchOne = false;
        initializeDescription();
    }



    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        //Default color
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

        // Odd amount of cards played before
        if (CardUtils.isOddPosition()) {
            this.glowColor = ColorUtils.MAGENTA_GLOW.cpy();
        }
        if (CardUtils.isPerfectPosition()) {

            this.glowColor = ColorUtils.GOLD_GLOW.cpy();
        }

        if (CardUtils.isNPosition(2)&&!upgradeBranchOne) {
            // Set the glow color to gold
            this.glowColor = ColorUtils.GOLD_GLOW.cpy();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlazingGuard();
    }
}
