package mandarinamod.monsters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AbstractIntentFriendlyMonster extends AbstractFriendlyMonster {
    // Reflection caches
    private static Method renderIntentMethod;
    private static Method renderIntentVfxBehindMethod;
    private static Method renderIntentVfxAfterMethod;
    private static Method renderDamageRangeMethod;

    private static Field intentTipField;
    private static Field intentDmgField;
    private static Field intentMultiAmtField;
    private static Field isMultiDmgField;

    static {
        try {
            // Private render methods
            renderIntentMethod = AbstractMonster.class.getDeclaredMethod("renderIntent", SpriteBatch.class);
            renderIntentVfxBehindMethod = AbstractMonster.class.getDeclaredMethod("renderIntentVfxBehind", SpriteBatch.class);
            renderIntentVfxAfterMethod = AbstractMonster.class.getDeclaredMethod("renderIntentVfxAfter", SpriteBatch.class);
            renderDamageRangeMethod = AbstractMonster.class.getDeclaredMethod("renderDamageRange", SpriteBatch.class);

            renderIntentMethod.setAccessible(true);
            renderIntentVfxBehindMethod.setAccessible(true);
            renderIntentVfxAfterMethod.setAccessible(true);
            renderDamageRangeMethod.setAccessible(true);

            // Private fields
            intentTipField = AbstractMonster.class.getDeclaredField("intentTip");
            intentDmgField = AbstractMonster.class.getDeclaredField("intentDmg");
            intentMultiAmtField = AbstractMonster.class.getDeclaredField("intentMultiAmt");
            isMultiDmgField = AbstractMonster.class.getDeclaredField("isMultiDmg");

            intentTipField.setAccessible(true);
            intentDmgField.setAccessible(true);
            intentMultiAmtField.setAccessible(true);
            isMultiDmgField.setAccessible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AbstractIntentFriendlyMonster(
            String name, String id, int maxHealth,
            float hb_x, float hb_y, float hb_w, float hb_h,
            String imgUrl, float offsetX, float offsetY
    ) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        if (!this.isDying && !this.isEscaping &&
                AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT &&
                !AbstractDungeon.player.isDead &&
                !AbstractDungeon.player.hasRelic("Runic Dome") &&
                this.intent != AbstractMonster.Intent.NONE &&
                !Settings.hideCombatElements) {

            try {
                // ====== CUSTOM TOOLTIP HANDLING ======
                Object intentTip = intentTipField.get(this);
                if (intentTip != null) {
                    Class<?> powerTipClass = intentTip.getClass();
                    Field headerField = powerTipClass.getDeclaredField("header");
                    Field bodyField = powerTipClass.getDeclaredField("body");
                    Field imgField = powerTipClass.getDeclaredField("img");
                    headerField.setAccessible(true);
                    bodyField.setAccessible(true);
                    imgField.setAccessible(true);

                    int dmg = (int) intentDmgField.get(this);
                    boolean isMulti = (boolean) isMultiDmgField.get(this);
                    int multi = (int) intentMultiAmtField.get(this);

                    if (this.intent == Intent.ATTACK) {
                        headerField.set(intentTip, "Aggressive");
                        if (isMulti) {
                            bodyField.set(intentTip, "This ally intends to #yAttack for #b"
                                    + dmg + " damage #b" + multi + " times.");
                        } else {
                            bodyField.set(intentTip, "This ally intends to #yAttack for #b"
                                    + dmg + " damage.");
                        }
                        //imgField.set(intentTip, ImageMaster.INTENT_ATTACK);

                    } else if (this.intent == Intent.DEFEND) {
                        headerField.set(intentTip, "Defensive");
                        bodyField.set(intentTip, "This ally intends to gain #yBlock.");
                        imgField.set(intentTip, ImageMaster.INTENT_DEFEND);

                    } else if (this.intent == Intent.BUFF) {
                        headerField.set(intentTip, "Ally Buff");
                        bodyField.set(intentTip, "This ally intends to strengthen themselves or you.");
                        imgField.set(intentTip, ImageMaster.INTENT_BUFF);

                    } else if (this.intent == Intent.DEBUFF) {
                        headerField.set(intentTip, "Ally Debuff");
                        bodyField.set(intentTip, "This ally intends to weaken the enemy.");
                        imgField.set(intentTip, ImageMaster.INTENT_DEBUFF);

                    }
                    // Unchanged for other intent types
                }

                // ====== RENDER INTENT VISUALS ======
                renderIntentVfxBehindMethod.invoke(this, sb);
                renderIntentMethod.invoke(this, sb);
                renderIntentVfxAfterMethod.invoke(this, sb);
                renderDamageRangeMethod.invoke(this, sb);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
