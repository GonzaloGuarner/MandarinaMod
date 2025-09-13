package mandarinamod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.curse.Languor;

public class TheOrchard extends PhasedEvent {
    public static final String ID = MandarinaMod.makeID(TheOrchard.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private int damage = 0;

    public TheOrchard() {
        super(ID, NAME, "mandarinamod/images/events/TheOrchard.png");

        if (AbstractDungeon.ascensionLevel >= 15) {
            this.damage = MathUtils.round((float)AbstractDungeon.player.maxHealth * 0.4F);
        } else {
            this.damage = MathUtils.round((float)AbstractDungeon.player.maxHealth * 0.3F);
        }

        // Closing Phase: Just shows the second description and a button to leave.
        TextPhase closingPhase = new TextPhase(DESCRIPTIONS[1])
                .addOption("Leave", (i) -> openMap());

        // Start Phase: Shows initial description and three options
        TextPhase startPhase = new TextPhase(DESCRIPTIONS[0])

                // [Withered Apple] Fully heal, but gain 2 Languor curses.
                .addOption(OPTIONS[0], (i) -> {
                    AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
                    AbstractCard languor1 = new Languor();
                    AbstractCard languor2 = new Languor();
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(languor1, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(languor2, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                    // Log metrics
                    logMetricObtainCard(ID, "Withered Apple", languor1);
                    logMetricObtainCard(ID, "Withered Apple", languor2);
                    transitionKey("Closing");
                })

                // [Golden Citrus] Gain 100 Gold, lose a random card.
                .addOption(OPTIONS[1], (i) -> {
                    AbstractDungeon.player.gainGold(100);
                    AbstractDungeon.effectList.add(new RainingGoldEffect(100));
                    AbstractCard card = AbstractDungeon.player.masterDeck.getRandomCard(true);
                    if (card != null) {
                        AbstractDungeon.player.masterDeck.removeCard(card);
                        logMetricCardRemoval(ID, "Golden Citrus", card);
                    }
                    logMetricGainGold(ID, "Golden Citrus", 100);
                    transitionKey("Closing");
                })

                // [Forbidden Fruit] Gain 2 Relics, lose 30% max HP.
                .addOption(OPTIONS[2]+damage+OPTIONS[3], (i) -> {
                    AbstractRelic relic1 = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON);
                    AbstractRelic.RelicTier[] possibleTiers = {
                            AbstractRelic.RelicTier.COMMON,
                            AbstractRelic.RelicTier.UNCOMMON,
                            AbstractRelic.RelicTier.RARE
                    };
                    AbstractRelic.RelicTier randomTier = possibleTiers[AbstractDungeon.miscRng.random(possibleTiers.length - 1)];
                    AbstractRelic relic2 = AbstractDungeon.returnRandomRelic(randomTier);

                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, relic1);
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, relic2);
                    AbstractDungeon.player.damage(new DamageInfo(null, (int) (AbstractDungeon.player.maxHealth * 0.3)));
                    logMetricObtainRelicAndDamage(ID, "Verboten Fruit", relic1, 30);
                    transitionKey("Closing");
                });

        // Register both phases
        registerPhase("Start", startPhase);
        registerPhase("Closing", closingPhase);

        // Begin the event at the Start phase
        transitionKey("Start");
    }

    @Override
    public void onEnterRoom() {
        CardCrawlGame.music.playTempBGM("SHRINE");
    }
}
