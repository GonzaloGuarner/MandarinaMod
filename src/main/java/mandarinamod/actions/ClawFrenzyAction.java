package mandarinamod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import mandarinamod.util.CardUtils;
import mandarinamod.vfx.combat.ClawEffect;

public class ClawFrenzyAction extends AbstractGameAction {
    private final AbstractPlayer p;
    private final AbstractMonster m;
    private final int damage;
    private final DamageInfo.DamageType damageType;
    private final boolean freeToPlayOnce;
    private final int energyOnUse;

    public ClawFrenzyAction(AbstractPlayer p, AbstractMonster m, int damage, DamageInfo.DamageType damageType, boolean freeToPlayOnce, int energyOnUse) {
        this.p = p;
        this.m = m;
        this.damage = damage;
        this.damageType = damageType;
        this.freeToPlayOnce = freeToPlayOnce;
        this.energyOnUse = energyOnUse;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (p.hasRelic("Chemical X")) {
            effect += 2;
            p.getRelic("Chemical X").flash();
        }
        boolean extraHit = CardUtils.isEvenPosition();
        if (extraHit) {
            effect += 1;
        }
        if (effect > 0) {
            for (int i = 0; i < effect; i++) {
                ClawEffect.SlashDirection direction;

                // Randomly select between DIAGONAL_UP and DIAGONAL_DOWN for each hit
                if (extraHit && i == effect - 1) {
                    direction = ClawEffect.SlashDirection.DOWN;
                } else {
                    direction = Math.random() < 0.5 ? ClawEffect.SlashDirection.DIAGONAL_UP : ClawEffect.SlashDirection.DIAGONAL_DOWN;
                }

                // Add visual effect
                AbstractDungeon.actionManager.addToBottom(
                        new VFXAction(new ClawEffect(m.hb.cX, m.hb.cY, direction, Color.RED, Color.WHITE))
                );

                // Deal damage
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAction(m, new DamageInfo(p, this.damage, this.damageType), AttackEffect.NONE)
                );
            }

            if (!this.freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount);
            }
        }

        this.isDone = true;
    }
}
