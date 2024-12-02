package mandarinamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class CracklingArcAction extends AbstractGameAction {
    private DamageInfo info;
    private int baseDamage;  // Full damage will reapply if an enemy dies

    public CracklingArcAction(AbstractMonster target, DamageInfo info) {
        this.info = info;
        this.setValues(target, info);
        this.baseDamage = info.base;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST && this.target != null) {
            AbstractMonster targetMonster = (AbstractMonster) this.target;

            // Trigger visual effect and apply the damage
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.LIGHTNING));
            this.target.damage(this.info);

            // Check if the target was killed
            if (targetMonster.isDying || targetMonster.currentHealth <= 0) {
                if (baseDamage > 0) {
                    // Find the next valid enemy
                    for (AbstractMonster nextTarget : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        if (!nextTarget.isDeadOrEscaped() && nextTarget != targetMonster) {
                            this.addToBot(new CracklingArcAction(nextTarget, new DamageInfo(this.info.owner, baseDamage, this.info.type)));
                            break;
                        }
                    }
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }
}

