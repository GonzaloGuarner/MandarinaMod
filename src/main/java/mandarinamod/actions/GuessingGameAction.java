package mandarinamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.AbstractCard;
import mandarinamod.util.CardUtils;

public class GuessingGameAction extends AbstractGameAction {
    private final AbstractCard.CardType guessedType;

    public GuessingGameAction(AbstractCard.CardType guessedType) {
        this.guessedType = guessedType;
    }

    @Override
    public void update() {
        AbstractPlayer player = AbstractDungeon.player;

        if (!player.drawPile.isEmpty()) {
            AbstractCard topCard = player.drawPile.getTopCard();
            boolean isCorrect = false;

            // Check the top card's type against the guessed type
            switch (guessedType) {
                case ATTACK:
                    isCorrect = (topCard.type == AbstractCard.CardType.ATTACK);
                    break;
                case SKILL:
                    isCorrect = (topCard.type == AbstractCard.CardType.SKILL);
                    break;
                case POWER: // "Neither" is mapped to STATUS and includes Power, Status, and Curse
                    isCorrect = (topCard.type == AbstractCard.CardType.POWER ||
                            topCard.type == AbstractCard.CardType.STATUS ||
                            topCard.type == AbstractCard.CardType.CURSE);
                    break;
            }

            // Handle the rewards if the guess was correct
            if (isCorrect) {
                if (CardUtils.isEvenPosition() || CardUtils.isPerfectPosition()) {
                    addToBot(new GainEnergyAction(3));// Gain 3 Energy if Even
                }
                if  (CardUtils.isOddPosition() || CardUtils.isPerfectPosition()) {
                    addToBot(new DrawCardAction(player, 3)); // Draw 3 cards if Odd
                }
            }
        }

        isDone = true;
    }

}

