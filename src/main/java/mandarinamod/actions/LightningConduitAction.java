package mandarinamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostForTurnAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;

public class LightningConduitAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int cardsToReduce;
    private final boolean freeToPlayOnce;
    private int energyOnUse;

    public LightningConduitAction(AbstractPlayer player, int cardsToReduce, int energyOnUse, boolean freeToPlayOnce) {
        this.player = player;
        this.cardsToReduce = cardsToReduce;
        this.energyOnUse = energyOnUse;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        // Handle Chemical X interaction
        if (this.player.hasRelic("Chemical X")) {
            effect += 2;
            this.player.getRelic("Chemical X").flash();
        }

        // Draw X cards
        this.addToBot(new DrawCardAction(player, effect));

        // Select random cards and reduce their cost by X for this turn
        ArrayList<AbstractCard> handCards = new ArrayList<>(player.hand.group);
        for (int i = 0; i < cardsToReduce && !handCards.isEmpty(); i++) {
            AbstractCard cardToReduce = handCards.remove(AbstractDungeon.cardRandomRng.random(handCards.size() - 1));
            this.addToBot(new ReduceCostForTurnAction(cardToReduce, 1));
        }

        // Spend the energy unless it's free to play once
        if (!this.freeToPlayOnce) {
            player.energy.use(EnergyPanel.totalCount);
        }

        this.isDone = true;
    }
}
