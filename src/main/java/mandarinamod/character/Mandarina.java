package mandarinamod.character;

import basemod.BaseMod;
import basemod.abstracts.CustomEnergyOrb;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import hlysine.friendlymonsters.characters.AbstractPlayerWithMinions;
import mandarinamod.cards.basic.*;
import mandarinamod.cards.common.EbbAndFlow;
import mandarinamod.cards.rare.AuroraRay;
import mandarinamod.cards.rare.DarklingPact;
import mandarinamod.cards.rare.ShadowAspect;
import mandarinamod.cards.rare.Tornado;
import mandarinamod.cards.uncommon.Fireballs;
import mandarinamod.cards.uncommon.GoWithTheFlow;
import mandarinamod.cards.uncommon.OddSmokes;
import mandarinamod.cards.uncommon.ShotInTheDark;
import mandarinamod.relics.TheCycleCompass;

import java.util.ArrayList;

import static mandarinamod.MandarinaMod.characterPath;
import static mandarinamod.MandarinaMod.makeID;

public class Mandarina extends AbstractPlayerWithMinions {
    //Stats
    public static final int ENERGY_PER_TURN = 3;
    public static final int MAX_HP = 77;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    //Strings
    private static final String ID = makeID("Mandarina"); //This should match whatever you have in the CharacterStrings.json file
    private static String[] getNames() { return CardCrawlGame.languagePack.getCharacterString(ID).NAMES; }
    private static String[] getText() { return CardCrawlGame.languagePack.getCharacterString(ID).TEXT; }

    //This static class is necessary to avoid certain quirks of Java classloading when registering the character.
    public static class Meta {
        //These are used to identify your character, as well as your character's card color.
        //Library color is basically the same as card color, but you need both because that's how the game was made.
        @SpireEnum
        public static PlayerClass MANDARINA;
        @SpireEnum(name = "CHARACTER_ORANGE_COLOR") // These two MUST match. Change it to something unique for your character.
        public static AbstractCard.CardColor CARD_COLOR;
        @SpireEnum(name = "CHARACTER_ORANGE_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;

        //Character select images
        private static final String CHAR_SELECT_BUTTON = characterPath("select/button.png");
        private static final String CHAR_SELECT_PORTRAIT = characterPath("select/portrait4.png");

        //Character card images
        private static final String BG_ATTACK = characterPath("cardback/bg_attack.png");
        private static final String BG_ATTACK_P = characterPath("cardback/bg_attack_p.png");
        private static final String BG_SKILL = characterPath("cardback/bg_skills.png");
        private static final String BG_SKILL_P = characterPath("cardback/bg_skills_p.png");
        private static final String BG_POWER = characterPath("cardback/bg_power.png");
        private static final String BG_POWER_P = characterPath("cardback/bg_power_p.png");
        private static final String ENERGY_ORB = characterPath("cardback/energy_orb.png");
        private static final String ENERGY_ORB_P = characterPath("cardback/energy_orb_p.png");
        private static final String SMALL_ORB = characterPath("cardback/small_orb.png");

        //This is used to color *some* images, but NOT the actual cards. For that, edit the images in the cardback folder!
        private static final Color cardColor = new Color(0.98f, 0.302f, 0.047f,1f);

        //Methods that will be used in the main mod file
        public static void registerColor() {
            BaseMod.addColor(CARD_COLOR, cardColor,
                    BG_ATTACK, BG_SKILL, BG_POWER, ENERGY_ORB,
                    BG_ATTACK_P, BG_SKILL_P, BG_POWER_P, ENERGY_ORB_P,
                    SMALL_ORB);
        }

        public static void registerCharacter() {
            BaseMod.addCharacter(new Mandarina(), CHAR_SELECT_BUTTON, CHAR_SELECT_PORTRAIT);
        }
    }


    //In-game images
    private static final String SHOULDER_1 = characterPath("shoulder.png"); //Shoulder 1 and 2 are used at rest sites.
    private static final String SHOULDER_2 = characterPath("shoulder2.png");
    private static final String CORPSE = characterPath("corpse.png"); //Corpse is when you die.

    //Textures used for the energy orb
    private static final String[] orbTextures = {
            characterPath("energyorb/layer1.png"), //When you have energy
            characterPath("energyorb/layer2.png"),
            characterPath("energyorb/layer3.png"),
            characterPath("energyorb/layer4.png"),
            characterPath("energyorb/layer5.png"),
            characterPath("energyorb/cover.png"), //"container"
            characterPath("energyorb/layer1d.png"), //When you don't have energy
            characterPath("energyorb/layer2d.png"),
            characterPath("energyorb/layer3d.png"),
            characterPath("energyorb/layer4d.png"),
            characterPath("energyorb/layer5d.png")
    };

    //Speeds at which each layer of the energy orb texture rotates. Negative is backwards.
    private static final float[] layerSpeeds = new float[] {
            -20.0F,
            20.0F,
            -40.0F,
            40.0F,
            360.0F
    };


    //Actual character class code below this point

    public Mandarina() {
        super(getNames()[0], Meta.MANDARINA,
                new CustomEnergyOrb(orbTextures, characterPath("energyorb/vfx.png"), layerSpeeds), //Energy Orb
                new SpriterAnimation(characterPath("animation/mandarinacomplex.scml"))); //Animation

        initializeClass(characterPath(null), //"MandarinaStaticSmallest.png"),
                SHOULDER_2,
                SHOULDER_1,
                CORPSE,
                getLoadout(),
                0.0F, -20.0F, 200.0F, 250.0F, //Character hitbox. x y position, then width and height.
                new EnergyManager(ENERGY_PER_TURN));

        //Location for text bubbles. You can adjust it as necessary later. For most characters, these values are fine.
        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 220.0F * Settings.scale);

        setBaseMinionCount(1);
        setBaseMinionPowerChance(0.5f);
        setBaseMinionAttackTargetChance(0.5f);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        //List of IDs of cards for your starting deck.
        //If you want multiple of the same card, you have to add it multiple times.
        retVal.add(ShotInTheDark.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
//        retVal.add(Defend.ID);
//        retVal.add(Defend.ID);
//        retVal.add(Defend.ID);
//        retVal.add(Defend.ID);
//        retVal.add(SwiftJab.ID);
//        retVal.add(Recall.ID);
//        retVal.add(PastPresentFuture.ID);
//        retVal.add(PositionalStrike.ID);
//        retVal.add(PerfectPosition.ID);
//        retVal.add(BlazingGuard.ID);
//        retVal.add(BlazingGuard.ID);
//        retVal.add(BlazingGuard.ID);
//        retVal.add(BlazingGuard.ID);
//        retVal.add(Tornado.ID);
////        retVal.add(ReboundKick.ID);
//  //      retVal.add(OddOdds.ID);
////        retVal.add(AuroraRay.ID);
//        retVal.add(AuroraRay.ID);
////        retVal.add(LightningConduit.ID);
////        retVal.add(TimelyBoost.ID);
////        retVal.add(OddSmokes.ID);
////        retVal.add(BlazingGuard.ID);
////        retVal.add(PushThrough.ID);
////        retVal.add(ShadowRitual.ID);
//        retVal.add(GoWithTheFlow.ID);
//        //retVal.add(SparkingThings.ID);
//          //retVal.add(PhoenixTenacity.ID);
////        retVal.add(DragonsBreath.ID);
////        retVal.add(ShadowAspect.ID);
////        retVal.add(SmogWave.ID);
////        retVal.add(Defend.ID);
////        retVal.add(DrunkenMaster.ID);
//
////        retVal.add(LightningConduit.ID);
////        retVal.add(AuroraRay.ID);
////        retVal.add(StaticCharge.ID);
////        retVal.add(Blur.ID);
////        retVal.add(FlameAspect.ID);
////        retVal.add(MidfightFire.ID);
//       retVal.add(DarklingPact.ID);
////        retVal.add(HotLeafJuice.ID);
////        retVal.add(KindleSpirit.ID);
////        retVal.add(PhoenixTenacity.ID);
////        retVal.add(UnbalancingWinds.ID);
//        retVal.add(GoWithTheFlow.ID);
//        retVal.add(EbbAndFlow.ID);
//        retVal.add(Fireballs.ID);


        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        //IDs of starting relics. You can have multiple, but one is recommended.
        retVal.add(TheCycleCompass.ID);

        return retVal;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        //This card is used for the Gremlin card matching game.
        //It should be a non-strike non-defend starter card, but it doesn't have to be.
        return new Recall();
    }

    /*- Below this is methods that you should *probably* adjust, but don't have to. -*/

    @Override
    public int getAscensionMaxHPLoss() {
        return 4; //Max hp reduction at ascension 14+
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        //These attack effects will be used when you attack the heart.
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.LIGHTNING,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        };
    }

    private final Color cardRenderColor = Color.ORANGE.cpy(); //Used for some vfx on moving cards (sometimes) (maybe)
    private final Color cardTrailColor = Color.ORANGE.cpy(); //Used for card trail vfx during gameplay.
    private final Color slashAttackColor = Color.ORANGE.cpy(); //Used for a screen tint effect when you attack the heart.
    @Override
    public Color getCardRenderColor() {
        return cardRenderColor;
    }

    @Override
    public Color getCardTrailColor() {
        return cardTrailColor;
    }

    @Override
    public Color getSlashAttackColor() {
        return slashAttackColor;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        //Font used to display your current energy.
        //energyNumFontRed, Blue, Green, and Purple are used by the basegame characters.
        //It is possible to make your own, but not convenient.
        return FontHelper.energyNumFontRed;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        //This occurs when you click the character's button in the character select screen.
        //See SoundMaster for a full list of existing sound effects, or look at BaseMod's wiki for adding custom audio.
        CardCrawlGame.sound.playA("ATTACK_DAGGER_2", MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        //Similar to doCharSelectScreenSelectEffect, but used for the Custom mode screen. No shaking.
        return "ATTACK_DAGGER_2";
    }

    //Don't adjust these four directly, adjust the contents of the CharacterStrings.json file.
    @Override
    public String getLocalizedCharacterName() {
        return getNames()[0];
    }
    @Override
    public String getTitle(PlayerClass playerClass) {
        return getNames()[1];
    }
    @Override
    public String getSpireHeartText() {
        return getText()[1];
    }
    @Override
    public String getVampireText() {
        return getText()[2]; //Generally, the only difference in this text is how the vampires refer to the player.
    }

    /*- You shouldn't need to edit any of the following methods. -*/

    //This is used to display the character's information on the character selection screen.
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(getNames()[0], getText()[0],
                MAX_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this,
                getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return Meta.CARD_COLOR;
    }

    @Override
    public AbstractPlayer newInstance() {
        //Makes a new instance of your character class.
        return new Mandarina();
    }
}
