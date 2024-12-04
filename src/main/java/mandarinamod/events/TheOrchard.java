package mandarinamod.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Clumsy;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.rare.ShadowAspect;
import mandarinamod.cards.curse.Languor;

public class TheOrchard extends AbstractImageEvent {
    public static final String ID = MandarinaMod.makeID("TheOrchard");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private CUR_SCREEN screen;

    public TheOrchard() {
        super(NAME, DESCRIPTIONS[0], "mandarinamod/images/events/TheOrchard.png");
        this.screen = CUR_SCREEN.INTRO;

        imageEventText.setDialogOption(OPTIONS[0]); // Gain two relics, lose 30% HP
        imageEventText.setDialogOption(OPTIONS[1]); // Heal fully, add two Languor cards
        imageEventText.setDialogOption(OPTIONS[2]); // Gain 100 gold, lose a random card
        imageEventText.setDialogOption(OPTIONS[3]); // Gain Shadow Aspect card
        imageEventText.setDialogOption(OPTIONS[4]); // Remove two curses, add random negative status
    }

    public void onEnterRoom() {
        CardCrawlGame.music.playTempBGM("SHRINE");
    }

    public void update() {
        super.update();
        if (!RoomEventDialog.waitForInput) {
            this.buttonEffect(this.roomEventText.getSelectedOption());
        }

    }


    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0: // Gain two relics, lose 30% of current HP
                        this.screen = CUR_SCREEN.COMPLETE;
                        AbstractRelic relic1 = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON);
                        AbstractRelic relic2 = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, relic1);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, relic2);
                        AbstractDungeon.player.damage(new DamageInfo(null, (int) (AbstractDungeon.player.maxHealth * 0.3)));
                        logMetricObtainRelicAndDamage("TheOrchard", "Bountiful Harvest", relic1, 30);
                        break;

                    case 1: // Heal fully, add two Languor cards
                        this.screen = CUR_SCREEN.COMPLETE;
                        AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
                        AbstractCard languor1 = new Languor();
                        AbstractCard languor2 = new Languor();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(languor1, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(languor2, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        logMetricObtainCard("TheOrchard", "Healing Berry", languor1);
                        logMetricObtainCard("TheOrchard", "Healing Berry", languor2);
                        break;

                    case 2: // Gain 100 gold, lose a random card
                        this.screen = CUR_SCREEN.COMPLETE;
                        AbstractDungeon.player.gainGold(100);
                        AbstractDungeon.effectList.add(new RainingGoldEffect(100));
                        AbstractCard card = AbstractDungeon.player.masterDeck.getRandomCard(true);
                        if (card != null) {
                            AbstractDungeon.player.masterDeck.removeCard(card);
                            logMetricCardRemoval("TheOrchard", "Golden Citrus", card);
                        }
                        logMetricGainGold("TheOrchard", "Golden Citrus", 100);
                        break;

                    case 3: // Gain Shadow Aspect card
                        this.screen = CUR_SCREEN.COMPLETE;
                        AbstractCard shadowAspect = new ShadowAspect();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(shadowAspect, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        logMetricObtainCard("TheOrchard", "Shadow Aspect", shadowAspect);
                        break;

                    case 4: // Remove two curses but gain a random negative status
                        this.screen = CUR_SCREEN.COMPLETE;
                        AbstractCard curse1 = AbstractDungeon.player.masterDeck.getRandomCard(AbstractCard.CardType.CURSE, true);
                        AbstractCard curse2 = AbstractDungeon.player.masterDeck.getRandomCard(AbstractCard.CardType.CURSE, true);
                        if (curse1 != null) {
                            AbstractDungeon.player.masterDeck.removeCard(curse1);
                        }
                        if (curse2 != null && curse2 != curse1) {
                            AbstractDungeon.player.masterDeck.removeCard(curse2);
                        }
                        AbstractCard negativeStatus = new Clumsy(); // Adding a negative status card as a consequence.
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(negativeStatus, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        this.screen = CUR_SCREEN.COMPLETE;
                        logMetricIgnored(ID);
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;

                    default:
                        break;
                }
            case COMPLETE:
                this.openMap();
        }

    }

    private enum CUR_SCREEN {
        INTRO,
        COMPLETE;

        CUR_SCREEN() {
        }
    }
}
