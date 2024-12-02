package mandarinamod.powers;

import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModContainer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import hlysine.friendlymonsters.characters.AbstractPlayerWithMinions;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;

import java.util.ArrayList;

import static mandarinamod.MandarinaMod.makeID;

public class ShadowAspectPower extends BasePower {
    public static final String POWER_ID = makeID(ShadowAspectPower.class.getSimpleName());
    private final AbstractCard blockCard;
    public ShadowAspectPower(AbstractCard blockCard, AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.blockCard = blockCard;
        this.updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        // At the start of the turn, all enemies gain block
        flash();
        AbstractDungeon.getMonsters().monsters.forEach(monster -> {
            addToBot(new GainCustomBlockAction(blockCard, monster, this.amount * 2)); // Enemies gain block
        });

        // Player and any friendly minions also gain block
        addToBot(new GainCustomBlockAction(blockCard, AbstractDungeon.player, this.amount * 2));

        if (AbstractDungeon.player instanceof AbstractPlayerWithMinions){
            MonsterGroup monstersGroup = ((AbstractPlayerWithMinions)AbstractDungeon.player).getMinions();
            if(!monstersGroup.monsters.isEmpty()){
                addToBot(new GainCustomBlockAction(blockCard, monstersGroup.monsters.get(0), this.amount * 2));
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            // At the end of the turn, the player steals block and gains a modified amount
            AbstractDungeon.getCurrRoom().monsters.monsters.forEach(monster -> {
                int stealingFactor = 3 - amount;
                stealingFactor = Math.max(1, stealingFactor); // Ensure divisor is not less than 1 to prevent division by zero
                int stolenBlock = monster.currentBlock / stealingFactor;

                if(stolenBlock > 0){
                    monster.loseBlock(stolenBlock);
                    addToBot(new GainBlockAction(owner, owner, stolenBlock));
                }
            });
            if (AbstractDungeon.player instanceof AbstractPlayerWithMinions){
                ArrayList<AbstractMonster> monsters = ((AbstractPlayerWithMinions)AbstractDungeon.player).getMinions().monsters;
                if(!monsters.isEmpty()){
                    AbstractMonster monster =  monsters.get(0);
                    int stealingFactor = 3 - amount;
                    stealingFactor = Math.max(1, stealingFactor); // Ensure divisor is not less than 1 to prevent division by zero
                    int stolenBlock = monster.currentBlock / stealingFactor;

                    if(stolenBlock > 0){
                        monster.loseBlock(stolenBlock);
                        addToBot(new GainBlockAction(owner, owner, stolenBlock));
                    }
                }
            }
            flash();
        }
    }

    @Override
    public void updateDescription() {
        if(this.amount == 1){
            description = DESCRIPTIONS[0] + 2*this.amount + DESCRIPTIONS[1] + DESCRIPTIONS[2];
        }else{
            description = DESCRIPTIONS[0] + 2*this.amount + DESCRIPTIONS[1] + DESCRIPTIONS[3];
        }
    }
}