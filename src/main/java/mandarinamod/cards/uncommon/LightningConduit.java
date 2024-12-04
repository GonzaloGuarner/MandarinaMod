package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostForTurnAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.actions.LightningConduitAction;

import java.util.ArrayList;

public class LightningConduit extends BaseCard {
    public static final String ID = MandarinaMod.makeID(LightningConduit.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -1 // X-cost card
    );

    public LightningConduit() {
        super(ID, info);
        this.exhaust = true;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster m) {
        //this.addToBot(new LightningConduitAction(p, this.energyOnUse, this.energyOnUse, this.freeToPlayOnce));

        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        // Handle Chemical X interaction
        if (player.hasRelic("Chemical X")) {
            effect += 2;
            player.getRelic("Chemical X").flash();
        }

        // Draw X cards
        this.addToBot(new DrawCardAction(player, effect));

        // Select random cards and reduce their cost by X for this turn
        ArrayList<AbstractCard> handCards = new ArrayList<>(player.hand.group);
        for (int i = 0; i < effect && !handCards.isEmpty(); i++) {
            AbstractCard cardToReduce = handCards.remove(AbstractDungeon.cardRandomRng.random(handCards.size() - 1));
            this.addToBot(new ReduceCostForTurnAction(cardToReduce, 1));
        }

        // Spend the energy unless it's free to play once
        if (!this.freeToPlayOnce) {
            player.energy.use(EnergyPanel.totalCount);
        }
        if(upgraded){
            addToBot((new GainEnergyAction(1)));
        }
    }


    @Override
    public AbstractCard makeCopy() {
        return new LightningConduit();
    }
}
