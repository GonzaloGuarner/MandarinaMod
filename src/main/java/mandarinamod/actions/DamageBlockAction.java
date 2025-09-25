package mandarinamod.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.characters.AbstractPlayer;


public class DamageBlockAction extends AbstractGameAction {
    private final AbstractPlayer sourcePlayer;

    public DamageBlockAction(AbstractCreature target, AbstractPlayer source, int amount) {
        this.target = target;
        this.duration = Settings.ACTION_DUR_FAST;
        this.sourcePlayer = source;
        this.amount = amount;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if(duration == startDuration) {
            if (target != null && !target.isDeadOrEscaped()) {
                int block = target.currentBlock;
                if (block > 0) {
                    int dmg = Math.min(block, this.amount);
                    DamageInfo info = new DamageInfo(sourcePlayer, dmg, DamageInfo.DamageType.NORMAL);
                    addToTop(new DamageAction(target, info, AttackEffect.NONE));
                }
            }
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
            this.duration = 0.0F;
            if (!Settings.FAST_MODE) {
                this.addToTop(new WaitAction(0.05F));
            }
        }
    }
}
