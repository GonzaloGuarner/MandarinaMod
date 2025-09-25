package mandarinamod.util;

import basemod.BaseMod;
import mandarinamod.MandarinaMod;

import static mandarinamod.MandarinaMod.makeID;
import static mandarinamod.MandarinaMod.soundPath;

public class SoundManager {
    public static final String AURORA = makeID("AURORA_THUNDER");
    public static final String MEGASHWOOSH1 = makeID("MEGASHWOOSH1");
    public static final String MEGASHWOOSH2 = makeID("MEGASHWOOSH2");
    public static final String STRONG_WIND = makeID("STRONG_WIND");

    private static final String AURORA_OGG = getOggFromName("aurora_thunder");
    private static final String MEGASHWOOSH1_OGG = getOggFromName("megaswosh1");
    private static final String MEGASHWOOSH2_OGG = getOggFromName("megaswosh2");
    private static final String STRONG_WIND_OGG = getOggFromName("strong_wind");

        // Call this from the receiver in your main mod class
        public static void addSounds() {
            BaseMod.addAudio(AURORA, AURORA_OGG);
            BaseMod.addAudio(MEGASHWOOSH1, MEGASHWOOSH1_OGG);
            BaseMod.addAudio(MEGASHWOOSH2, MEGASHWOOSH2_OGG);
            BaseMod.addAudio(STRONG_WIND, STRONG_WIND_OGG);
        }

    public static String getOggFromName(String fileName) {
        return soundPath( fileName + ".ogg");
    }

}