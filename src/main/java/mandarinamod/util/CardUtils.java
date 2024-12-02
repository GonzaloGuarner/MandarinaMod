package mandarinamod.util;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mandarinamod.powers.FlameAspectPower;
import mandarinamod.powers.GustAspectPower;
import mandarinamod.powers.ShadowAspectPower;
import mandarinamod.powers.ThunderAspectPower;

public class CardUtils {
    /**
     * Checks if the current card play position is odd.
     * @return true if Odd, false otherwise.
     */
    public static boolean isOddPosition() {
        int cardsPlayedThisTurn = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        return cardsPlayedThisTurn % 2 == 1;
    }

    /**
     * Checks if the current card play position is even (Pair).
     * @return true if Even, false otherwise.
     */
    public static boolean isEvenPosition() {
        int cardsPlayedThisTurn = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        return cardsPlayedThisTurn % 2 == 0;
    }

    public static boolean isFirstPosition() {
        return AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == 1;
    }

    public static boolean isNPosition(int n) {
        return AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == n;
    }

    public static boolean isThirdPosition() {
        return AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == 3;
    }
    private static boolean inPerfectPosition = false;
    public static boolean isPerfectPosition()
    {
        return inPerfectPosition;
    }
    public static void setPerfectPosition(boolean perfectPosition)
    {
        inPerfectPosition = perfectPosition;
    }

    public static boolean hasElementalPower() {
        return  AbstractDungeon.player.hasPower(GustAspectPower.POWER_ID) || AbstractDungeon.player.hasPower(ThunderAspectPower.POWER_ID) || AbstractDungeon.player.hasPower(FlameAspectPower.POWER_ID) || AbstractDungeon.player.hasPower(ShadowAspectPower.POWER_ID);
    }
}
