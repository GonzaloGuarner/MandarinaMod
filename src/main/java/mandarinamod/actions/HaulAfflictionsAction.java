package mandarinamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class HaulAfflictionsAction extends AbstractGameAction {
    private AbstractPlayer player;
    private int magicNumber;
    private DamageInfo.DamageType damageType;

    public HaulAfflictionsAction(AbstractPlayer player, int magicNumber, DamageInfo.DamageType damageType) {
        this.player = player;
        this.magicNumber = magicNumber;
        this.damageType = damageType;
    }

    @Override
    public void update() {
        // Calculate the damage for each monster based on the updated number of debuffs
        int numMonsters = AbstractDungeon.getMonsters().monsters.size();
        int[] damageArray = new int[numMonsters];

        for (int i = 0; i < numMonsters; i++) {
            AbstractMonster monster = AbstractDungeon.getMonsters().monsters.get(i);
            int debuffCount = (int) monster.powers.stream()
                    .filter(power -> power.type == AbstractPower.PowerType.DEBUFF)
                    .count();
            damageArray[i] = debuffCount * magicNumber;
            addToBot(new LoseHPAction(monster, player, damageArray[i], AttackEffect.POISON));
        }

        // Deal the calculated damage to all enemies
//        addToTop(new DamageAllEnemiesAction(player, damageArray, damageType, AttackEffect.POISON, true));

        isDone = true;
    }
}
