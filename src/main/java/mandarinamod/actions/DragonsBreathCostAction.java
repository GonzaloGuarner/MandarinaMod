package mandarinamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import mandarinamod.cards.rare.DragonsBreath;
import mandarinamod.powers.BurntPower;

public class DragonsBreathCostAction extends AbstractGameAction {
    private final DragonsBreath card;

    public DragonsBreathCostAction(DragonsBreath card) {
        this.card = card;
        this.duration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {

        MonsterGroup monsterGroup = AbstractDungeon.getMonsters();
        if (monsterGroup == null || monsterGroup.monsters.isEmpty()) {
            this.isDone = true;
            return;
        }

        int totalBurnt = monsterGroup.monsters.stream()
                .filter(monster -> !monster.isDeadOrEscaped())
                .mapToInt(monster -> monster.hasPower(BurntPower.POWER_ID)
                        ? monster.getPower(BurntPower.POWER_ID).amount
                        : 0)
                .sum();

        int threshold = card.customVar("burntthreshold");
        int modifiedCost = Math.max(0, card.BaseCost() - (totalBurnt / threshold));

        if (card.cost != modifiedCost) {
            card.setCostForTurn(modifiedCost);  // safer than updateCost()
        }

        this.isDone = true;
    }
}