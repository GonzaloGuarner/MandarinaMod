package mandarinamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ReplayLastCardAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final boolean chooseFromLastTwo;

    public ReplayLastCardAction(AbstractPlayer p, AbstractMonster m, boolean chooseFromLastTwo, boolean exhaust) {
        this.player = p;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.chooseFromLastTwo = chooseFromLastTwo;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (player.discardPile.size() == 0) {
                this.isDone = true;
                return;
            }

            AbstractCard cardToReplay = null;

            if (chooseFromLastTwo && player.discardPile.size() >= 2) {
                // Let the player choose between the last two cards
                cardToReplay = player.discardPile.group.get(player.discardPile.size() - 1);
                // Implement choice logic as needed
                // For simplicity, we'll replay the second last card
                cardToReplay = player.discardPile.group.get(player.discardPile.size() - 2);
            } else {
                // Replay the last card
                cardToReplay = player.discardPile.getTopCard();
            }

            if (cardToReplay != null) {
                player.discardPile.removeCard(cardToReplay);
                player.hand.addToTop(cardToReplay);
                cardToReplay.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                cardToReplay.target_y = Settings.HEIGHT / 2.0F;
                cardToReplay.current_x = Settings.WIDTH / 2.0F;
                cardToReplay.current_y = Settings.HEIGHT / 2.0F;
                cardToReplay.targetAngle = 0.0F;
                cardToReplay.lighten(false);
                cardToReplay.unhover();
                player.hand.refreshHandLayout();
                cardToReplay.applyPowers();
            }

            this.isDone = true;
        }
    }
}
