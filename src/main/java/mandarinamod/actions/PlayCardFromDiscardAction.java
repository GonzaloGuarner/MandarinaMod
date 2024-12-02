package mandarinamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class PlayCardFromDiscardAction extends AbstractGameAction {
    private int numberOfCardsToReplay;
    private int numberOfCardsToChooseFrom;

    public PlayCardFromDiscardAction(int numberOfCardsToReplay, int numberOfCardsToChooseFrom) {
        this.numberOfCardsToReplay = numberOfCardsToReplay;
        this.numberOfCardsToChooseFrom = numberOfCardsToChooseFrom;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            CardGroup discardPile = AbstractDungeon.player.discardPile;

            if (discardPile.isEmpty()) {
                this.isDone = true;
                return;
            }

            // Determine the actual number of cards available to choose from
            int availableCards = Math.min(numberOfCardsToChooseFrom, discardPile.size());

            // Get the last 'availableCards' from the discard pile
            ArrayList<AbstractCard> selectableCards = new ArrayList<>();
            for (int i = 0; i < availableCards; i++) {
                AbstractCard card = discardPile.group.get(discardPile.size() - 1 - i);
                selectableCards.add(card);
            }

            if (selectableCards.isEmpty()) {
                this.isDone = true;
                return;
            }

            if (selectableCards.size() == 1) {
                // Only one card to choose from or replaying one card
                AbstractCard cardToPlay = selectableCards.get(0);
                playCard(cardToPlay);
                this.isDone = true;
                return;
            }

            // Open a selection grid for the player
            CardGroup selectionGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            selectionGroup.group.addAll(selectableCards);

            AbstractDungeon.gridSelectScreen.open(
                    selectionGroup,
                    numberOfCardsToReplay,
                    "Choose a card to replay",
                    false
            );

            this.tickDuration();
        } else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            // Player has made a selection
            for (AbstractCard selectedCard : AbstractDungeon.gridSelectScreen.selectedCards) {
                playCard(selectedCard);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.isDone = true;
        } else {
            this.tickDuration();
        }
    }

    private void playCard(AbstractCard card) {
        // Remove the card from the discard pile
        AbstractDungeon.player.discardPile.removeCard(card);

        // Add the card to limbo (temporary zone for cards being played)
        AbstractDungeon.player.limbo.group.add(card);

        // Set the card to be free to play once
        card.freeToPlayOnce = true;

        // Set card's position and visual properties
        card.current_y = -200.0F * Settings.scale;
        card.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.xScale;
        card.target_y = (float) Settings.HEIGHT / 2.0F;
        card.targetAngle = 0.0F;
        card.lighten(false);
        card.drawScale = 0.12F;
        card.targetDrawScale = 0.75F;

        // Apply any powers (e.g., Strength, Dexterity)
        card.applyPowers();
        AbstractCreature target = AbstractDungeon.getCurrRoom().monsters.getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
        // Queue the card to be played
        this.addToTop(new NewQueueCardAction(card, target, false, true));

        // Remove the card from limbo after it's played
        this.addToTop(new UnlimboAction(card));

        // Optional: Add a brief wait to ensure animations play smoothly
        if (!Settings.FAST_MODE) {
            this.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
        } else {
            this.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
        }
    }
}
