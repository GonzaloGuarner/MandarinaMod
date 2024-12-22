package mandarinamod.relics;

import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModContainer;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.RelicStrings;
import mandarinamod.MandarinaMod;
import mandarinamod.actions.ReduceCostRandomAction;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.CursedBlock;

public class TheCycle extends BaseRelic {
    private static final String NAME = "TheCycle";
    public static final String ID = MandarinaMod.makeID(NAME);
    private static final RelicStrings strings = CardCrawlGame.languagePack.getRelicStrings(ID);

    private static final String[] ELEMENTS = {"Gust", "Thunder", "Flame", "Shadow"};
    private boolean gustPending = false;
    private boolean thunderPending = false;
    private boolean flamePending = false;
    private boolean shadowPending = false;
    private int initCounter = -1;

    public TheCycle() {
        super(ID, NAME, Mandarina.Meta.CARD_COLOR, RelicTier.STARTER, LandingSound.MAGICAL);
        this.counter = -1;
    }

    @Override
    public void atBattleStart() {
        flash();
        long seed = Settings.seed; // Get the base game seed
        int length = ELEMENTS.length;
        initCounter = (int) ((seed % length + length) % length); // Initialize the counter to zero
        if(counter == -1) this.counter = initCounter;
        int currentElementIndex = (this.counter) % length; // Progress with counter

        // Set the pending flag based on the current element
        gustPending = ELEMENTS[currentElementIndex].equals("Gust");
        thunderPending = ELEMENTS[currentElementIndex].equals("Thunder");
        flamePending = ELEMENTS[currentElementIndex].equals("Flame");
        shadowPending = ELEMENTS[currentElementIndex].equals("Shadow");

        updateDescription(currentElementIndex);
    }

    @Override
    public void atTurnStartPostDraw() {
        if (gustPending) {
            addToBot(new DrawCardAction(AbstractDungeon.player, 1));
            gustPending = false;
        }
        if (thunderPending) {
            addToBot(new GainGoldAction(7));
            thunderPending = false;
        }
        if (flamePending) {
            addToBot(new ReduceCostRandomAction());
            flamePending = false;
        }
        if (shadowPending) {
            AbstractDungeon.player.heal(2);
            addToBot(new GainBlockAction(AbstractDungeon.player, 2));
            addToBot(new GainCustomBlockAction(new BlockModContainer(this, new CursedBlock()), AbstractDungeon.player, 2));
            shadowPending = false;
        }
    }

    @Override
    public void onVictory() {
        // Increment the counter after each successful combat
        this.counter++;
        int currentElementIndex = (this.counter) % ELEMENTS.length;
        this.counter = currentElementIndex;
        updateDescription(currentElementIndex);
    }

    @Override
    public String getUpdatedDescription() {
 // Seed-based starting index
        return buildDescription(-1);
    }

    private void updateDescription(int currentElementIndex) {
        description = buildDescription(currentElementIndex);
        tips.clear();
        tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    private String buildDescription(int currentElementIndex) {
        StringBuilder description = new StringBuilder();
        description.append(strings.DESCRIPTIONS[0]); // "At the start of each combat, gain a bonus from the current element: "

        switch (currentElementIndex) {
            case 0:
                description.append(strings.DESCRIPTIONS[1]); // "Gust: Draw 1 card. NL "
                break;
            case 1:
                description.append(strings.DESCRIPTIONS[2]); // "Thunder: Gain 7 Gold. NL "
                break;
            case 2:
                description.append(strings.DESCRIPTIONS[3]); // "Flame: Reduce the cost of a random card in your hand by 1. NL "
                break;
            case 3:
                description.append(strings.DESCRIPTIONS[4]); // "Shadow: Heal 2 HP and gain 2 Block. NL "
                break;
            default:
                description.append(strings.DESCRIPTIONS[1]);
                description.append(strings.DESCRIPTIONS[2]);
                description.append(strings.DESCRIPTIONS[3]);
                description.append(strings.DESCRIPTIONS[4]);
                break;
        }

        description.append(strings.DESCRIPTIONS[5]); // "The elements cycle every combat."
        return description.toString();
    }
}
