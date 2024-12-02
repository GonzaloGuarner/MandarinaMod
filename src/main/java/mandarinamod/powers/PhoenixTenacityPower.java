package mandarinamod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;

public class PhoenixTenacityPower extends BasePower {
    public static final String POWER_ID = MandarinaMod.makeID(PhoenixTenacityPower.class.getSimpleName());

    public PhoenixTenacityPower(AbstractCreature owner, int burnAmount) {
        super(POWER_ID, PowerType.BUFF, false, owner, burnAmount);
        this.updateDescription();
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        // Apply burn if the drawn card is a status card
        if (card.type == AbstractCard.CardType.STATUS) {
            applyBurntToLeftmostEnemy();
            if (card.cardID.equals("Burn")) {
                addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
            }
        }
    }

    @Override
    public void onExhaust(AbstractCard card) {
        // Apply burn if the exhausted card is a status card
        if (card.type == AbstractCard.CardType.STATUS) {
            applyBurntToLeftmostEnemy();
        }
    }

    private void applyBurntToLeftmostEnemy() {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(monster, this.owner, new BurntPower(monster, this.amount), this.amount));
                return; // Stop after applying to the first alive enemy
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
