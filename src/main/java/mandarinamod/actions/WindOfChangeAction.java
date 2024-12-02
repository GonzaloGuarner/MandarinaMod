package mandarinamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.TransformCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;

public class WindOfChangeAction extends AbstractGameAction {
    private int amount;

    public WindOfChangeAction(int amount) {
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.handCardSelectScreen.open("Select cards to transform", this.amount, false, true, false, false, true);
            this.addToBot(new WaitAction(0.25F));
        } else if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for(AbstractCard c :  AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                int index = AbstractDungeon.handCardSelectScreen.selectedCards.group.indexOf(c);
                AbstractDungeon.actionManager.addToBottom(new TransformCardAction(c, getRandomTransformedCard(c)));
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        this.tickDuration();
    }

    private AbstractCard getRandomTransformedCard(AbstractCard originalCard) {
        AbstractCard transformedCard = AbstractDungeon.getCard(AbstractDungeon.rollRarity());
        if (originalCard.upgraded) {
            transformedCard.upgrade(); // Retain upgraded status
        }
        return transformedCard;
    }
}
