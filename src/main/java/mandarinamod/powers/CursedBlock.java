package mandarinamod.powers;


import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PoisonPower;
import mandarinamod.MandarinaMod;
import mandarinamod.util.TextureLoader;

import java.util.ArrayList;

import static mandarinamod.MandarinaMod.imagePath;

public class CursedBlock extends AbstractBlockModifier {
    public static final String ID = MandarinaMod.makeID("CursedBlock");
    private int totalLostBlock = 0;

    public CursedBlock() {}

    @Override
    public int amountLostAtStartOfTurn() {
        return this.getCurrentAmount(); // Fully lose all block at the end of the turn
    }

    @Override
    public void onThisBlockDamaged(DamageInfo info, int lostAmount) {
        // Accumulate the total blocks lost
        totalLostBlock += lostAmount;

        // Check if all blocks have been removed
        if (getCurrentAmount() <= 0) {
            // Calculate poison amount based on total blocks lost
            int poisonAmount = totalLostBlock / 2;

            // Apply poison if poisonAmount is greater than 0
            if (owner != null && poisonAmount > 0) {
                addToBot(new ApplyPowerAction(owner, owner, new PoisonPower(owner, AbstractDungeon.player, poisonAmount), poisonAmount));
            }

            // Reset the totalLostBlocks after applying poison
            totalLostBlock = 0;
        }
    }

    public void onApplication() {
    }
    public void onStack(int amount) {
    }


    @Override
    public void atStartOfTurnPreBlockLoss() {
        // Optional: Additional logic before block is reduced at the start of the turn
        //this.flash(); // Provide visual feedback for block type
    }

    @Override
    public void atEndOfRound() {
        totalLostBlock = 0;
    }

    @Override
    public Texture customBlockImage(){
        return TextureLoader.getTexture(imagePath("powers/hertss.png"));
    }
    @Override
    public Color blockImageColor() {
        // Use a shadowy purple tint for the block icon
        return new Color(0.28f,0.09f,0.00f,1.0f);
    }
    @Override
    public String getName() {
        return "Cursed Block";
    }

    @Override
    public String getDescription() {
        return "Protects from damage. If broken, gain Poison equal to half the block lost.";
    }

    @Override
    public boolean isInherent() {
        return true; // Automatically bind to cards or effects
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new CursedBlock(); // Ensure proper duplication for stacking or reapplication
    }

    @Override
    public ArrayList<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> tooltips = new ArrayList<>();
        tooltips.add(new TooltipInfo(getName(), getDescription()));
        return tooltips; // Add tooltip for UI clarity
    }
}
