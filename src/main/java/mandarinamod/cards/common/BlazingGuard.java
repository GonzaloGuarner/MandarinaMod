package mandarinamod.cards.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;
import mandarinamod.util.ColorUtils;
import mandarinamod.util.CustomTags;

public class BlazingGuard extends BaseCard {
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
    private static final int UPGRADE_BLOCK = 3;
    private static final int DRAW_AMOUNT = 1;
    private static final int UPGRADE_DRAW = 0;

    public BlazingGuard() {
        super(ID, info);
        setBlock(BLOCK, UPGRADE_BLOCK);
        setMagic(DRAW_AMOUNT, UPGRADE_DRAW);
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
                    if (upgraded) {
                        for (AbstractCard c : DrawCardAction.drawnCards)
                            addToTop(new UpgradeSpecificCardAction(c));
                    }
                    isDone = true;
                }
            }));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
            upgradeMagicNumber(UPGRADE_DRAW); // Upgrade to draw and upgrade 2 cards instead of 1
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        //Default color
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

        // Odd amount of cards played before
        if (CardUtils.isOddPosition()) {
            // Set the glow color to gold if card would be even
            this.glowColor = ColorUtils.MAGENTA_GLOW.cpy();
        }
        if (CardUtils.isPerfectPosition()) {
            // Set the glow color to gold
            this.glowColor = GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlazingGuard();
    }
}
