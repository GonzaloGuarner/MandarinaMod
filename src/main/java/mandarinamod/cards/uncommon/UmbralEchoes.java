// UmbralEchoes.java

package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.actions.PlayCardFromDiscardAction;
import mandarinamod.actions.PlayTopDiscardAction;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class UmbralEchoes extends BaseCard {
    public static final String ID = makeID(UmbralEchoes.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            1
    );

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    public UmbralEchoes() {
        super(ID, info);
        this.exhaust = true; // The card exhausts after use
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        if (upgraded) {
            // Allow the player to choose one of the last 2 cards in the discard pile to replay
            this.addToBot(new PlayCardFromDiscardAction(1, 3));
        } else {
            // Replay the last card played in the discard pile
            this.addToBot(new PlayTopDiscardAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng), false));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new UmbralEchoes();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
