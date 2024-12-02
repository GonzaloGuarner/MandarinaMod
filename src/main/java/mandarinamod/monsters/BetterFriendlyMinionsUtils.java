//// Of the copycat mod
//
//package mandarinamod.monsters;
//
//import com.badlogic.gdx.graphics.Color;
//import com.megacrit.cardcrawl.actions.AbstractGameAction;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.monsters.MonsterGroup;
//import com.megacrit.cardcrawl.random.Random;
//import  hlysine.friendlymonsters.characters.AbstractPlayerWithMinions;
//import  hlysine.friendlymonsters.enums.MonsterIntentEnum;
//import hlysine.friendlymonsters.helpers.BasePlayerMinionHelper;
//import hlysine.friendlymonsters.helpers.MonsterHelper;
//import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
//import hlysine.friendlymonsters.patches.PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch;
//
//import java.util.ArrayList;
//
//public class BetterFriendlyMinionsUtils {
//    public static AbstractFriendlyMonster[] copycatMinions = new AbstractFriendlyMonster[4];
//    public static Random minionAiRng = new Random();
//    public static Random enemyTargetRng = new Random();
//    public static Random dummyRng = new Random();
//
//    public static float prevPlayerhX;
//    public static float prevPlayerhY;
//    public static float prevPlayercX;
//    public static float prevPlayercY;
//    public static Random prevAiRng = null;
//    public static ArrayList<AbstractGameAction> origActions;
//    static HijackMode hijackMode = HijackMode.NONE;
//
//    public static MonsterGroup getMonsterGroup() {
//        return (AbstractDungeon.player instanceof AbstractPlayerWithMinions) ?
//                ((AbstractPlayerWithMinions) AbstractDungeon.player).getMinions() :
//                PlayerAddFieldsPatch.fm_minions.get(AbstractDungeon.player);
//    }
//
//    public static ArrayList<AbstractMonster> getMinionList() {
//        return getMonsterGroup().monsters;
//    }
//
//    public static int getNextCopycatMinionSlot() {
//        for (int i = 0; i < copycatMinions.length; i++) {
//            if (copycatMinions[i] != null && !copycatMinions[i].isDead) {
//                continue;
//            }
//            return i;
//        }
//        return -1;
//    }
//
//    public static boolean summonCopycatMinion(AbstractFriendlyMonster m) {
//        if (PlayerAddFieldsPatch.fm_maxMinionCount.get(AbstractDungeon.player) == 1) {
//            PlayerAddFieldsPatch.fm_maxMinionCount.set(AbstractDungeon.player, 4);
//        }
//
//        int index = getNextCopycatMinionSlot();
//        if (index == -1) {
//            return false;
//        }
//
//        boolean result;
//        if (AbstractDungeon.player instanceof AbstractPlayerWithMinions) {
//            result = ((AbstractPlayerWithMinions) AbstractDungeon.player).addMinion(m);
//        } else {
//            result = BasePlayerMinionHelper.addMinion(AbstractDungeon.player, m);
//        }
//        if (result) {
//            copycatMinions[index] = m;
//            m.setIndex(index);
//        }
//        return result;
//    }
//
//    /**
//     * Hijacks action queue.
//     *
//     * @param target redirect target
//     */
//    public static void hijackActionQueue(AbstractCreature target, HijackMode mode) {
//        if (target != null) {
//            prevPlayerhX = AbstractDungeon.player.hb.x;
//            prevPlayerhY = AbstractDungeon.player.hb.y;
//            prevPlayercX = AbstractDungeon.player.hb.cX;
//            prevPlayercY = AbstractDungeon.player.hb.cY;
//            AbstractDungeon.player.hb.move(target.hb.cX, target.hb.cY);
//            if (mode == HijackMode.FRIENDLY_MINION_ATTACK) {
//                prevAiRng = AbstractDungeon.aiRng;
//                AbstractDungeon.aiRng = minionAiRng;
//            }
//        }
//
//        hijackMode = mode;
//
//        origActions = AbstractDungeon.actionManager.actions;
//        AbstractDungeon.actionManager.actions = new ArrayList<>();
//    }
//
//    /**
//     * Reverts RNG and player hitbox position changed by hijackActionQueue().
//     * Note that this does not revert the actionManager.actions, you need to manually revert them.
//     *
//     * @return if revert was successful
//     */
//    public static HijackMode revertHijack() {
//        switch (hijackMode) {
//            case FRIENDLY_MINION_ATTACK:
//                if (prevAiRng != null) {
//                    AbstractDungeon.aiRng = prevAiRng;
//                    prevAiRng = null;
//                }
//                // fallthrough
//            case FRIENDLY_MINION_DEFEND:
//                AbstractDungeon.player.hb.x = prevPlayerhX;
//                AbstractDungeon.player.hb.y = prevPlayerhY;
//                AbstractDungeon.player.hb.cX = prevPlayercX;
//                AbstractDungeon.player.hb.cY = prevPlayercY;
//        }
//        HijackMode result = hijackMode;
//        hijackMode = HijackMode.NONE;
//        return result;
//    }
//
//    /**
//     * call MonsterHelper.switchTarget and shows TargetArrow.
//     */
//    public static void switchTarget(AbstractMonster monster, AbstractFriendlyMonster newTarget, boolean disableArrow) {
//        AbstractFriendlyMonster oldTarget = MonsterHelper.getTarget(monster);
//
//        if (oldTarget != newTarget) {
//            AbstractMonster.Intent intent = monster.intent;
//            if (oldTarget == null) {
//                if (intent == AbstractMonster.Intent.ATTACK) {
//                    monster.intent = MonsterIntentEnum.ATTACK_MINION;
//                } else if (intent == AbstractMonster.Intent.ATTACK_BUFF) {
//                    monster.intent = MonsterIntentEnum.ATTACK_MINION_BUFF;
//                } else if (intent == AbstractMonster.Intent.ATTACK_DEBUFF) {
//                    monster.intent = MonsterIntentEnum.ATTACK_MINION_DEBUFF;
//                } else if (intent == AbstractMonster.Intent.ATTACK_DEFEND) {
//                    monster.intent = MonsterIntentEnum.ATTACK_MINION_DEFEND;
//                }
//            }
//            MonsterHelper.switchTarget(monster, newTarget);
//            if (!disableArrow) {
//                AbstractDungeon.effectsQueue.add(new CopycatFlashTargetArrowEffect(monster, newTarget == null ? AbstractDungeon.player : newTarget, 0.75f));
//            }
//            DoubleTimePower.updateMinions();
//        }
//    }
//
//    public static AbstractCreature getHoveredAlly() {
//        ArrayList<AbstractCreature> allyList = getAllyList();
//        for (AbstractCreature c : allyList) {
//            c.hb.update();
//            if (c.hb.hovered) {
//                return c;
//            }
//        }
//        return null;
//    }
//
//    public static ArrayList<AbstractCreature> getAllyList() {
//        ArrayList<AbstractCreature> result = new ArrayList<>();
//        result.add(AbstractDungeon.player);
//        ArrayList<AbstractMonster> minionList = getMinionList();
//        result.addAll(minionList);
//        if (!SubstituteMinion.instance.isDead) {
//            result.add(SubstituteMinion.instance);
//        }
//        if (CopycatModMain.isDragonTamerLoaded) {
//            AbstractPlayer dragon = DTModCrossover.getLivingDragon();
//            if (dragon != null) {
//                result.add(dragon);
//            }
//        }
//        return result;
//    }
//
//    public static AbstractCreature getTarget(AbstractMonster m) {
//        AbstractCreature target = MonsterHelper.getTarget(m);
//        if (target == null) {
//            if (CopycatModMain.isDragonTamerLoaded) {
//                target = DTModCrossover.getCurrentTarget(m);
//            } else {
//                target = AbstractDungeon.player;
//            }
//        }
//        return target;
//    }
//
//
//    public enum HijackMode {
//        NONE,
//        FRIENDLY_MINION_ATTACK,
//        FRIENDLY_MINION_DEFEND,
//        MANIPULATE,
//    }
//}
