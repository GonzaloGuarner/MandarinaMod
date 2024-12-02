package mandarinamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import mandarinamod.MandarinaMod;

import java.util.ArrayList;
import java.util.Iterator;

public class TimelyBoostAction  extends AbstractGameAction {
    private AbstractPlayer p;
    private boolean upgraded = false;

    public TimelyBoostAction(boolean ultimateUpgradePlus) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.upgraded = ultimateUpgradePlus;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.upgraded) {
                // Upgrade all cards in hand
                for (AbstractCard card : this.p.hand.group) {
                    if (card.canUpgrade()) {
                        upgradeCardInHandAndDeck(card);
                    }
                }
                this.isDone = true;
                return;
            }

            // Regular upgrade: select one card to upgrade
            Iterator<AbstractCard> iterator = this.p.hand.group.iterator();
            ArrayList<AbstractCard> upgradeable = new ArrayList<>();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                if (card.canUpgrade()) {
                    upgradeable.add(card);
                }
            }

            if (upgradeable.isEmpty()) {
                this.isDone = true;
                return;
            }

            if (upgradeable.size() == 1) {
                // Only one card available to upgrade
                upgradeCardInHandAndDeck(upgradeable.get(0));
                this.isDone = true;
                return;
            }

            // Open selection to choose a card to upgrade
            AbstractDungeon.handCardSelectScreen.open("Select a card to upgrade permanently", 1, false, false, false, false);
            this.tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            // Upgrade the selected card
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                upgradeCardInHandAndDeck(card);
                this.p.hand.addToTop(card);
            }

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
        }

        this.tickDuration();
    }

    private void upgradeCardInHandAndDeck(AbstractCard card) {
        // Upgrade card in hand
        card.upgrade();
        card.superFlash();
        card.applyPowers();

        // Upgrade the corresponding card in the master deck
        for (AbstractCard masterDeckCard : AbstractDungeon.player.masterDeck.group) {
            if (masterDeckCard.uuid.equals(card.uuid)) {
                masterDeckCard.upgrade();
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(masterDeckCard.makeStatEquivalentCopy()));
                AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(masterDeckCard.current_x, masterDeckCard.current_y));
                break;
            }
        }
    }
}