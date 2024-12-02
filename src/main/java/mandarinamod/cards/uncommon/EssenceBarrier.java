package mandarinamod.cards.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardUtils;
import mandarinamod.util.CardStats;

public class EssenceBarrier extends BaseCard {
    public static final String ID = makeID(EssenceBarrier.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Specific card color for Mandarina
            CardType.SKILL, // Skill card
            CardRarity.UNCOMMON, // Uncommon card rarity
            CardTarget.SELF, // Targets the player
            1 // Costs 1 energy
    );

    // Base values for block
    private static final int BLOCK = 8;
    private static final int UPGRADE_BLOCK = 3;

    // Custom variable for extra block
    private static final int EXTRA_BLOCK = 3;
    private static final int UPGRADE_EXTRA_BLOCK = 1;

    public EssenceBarrier() {
        super(ID, info);

        // Set the base and upgrade values for block using BaseCard methods
        setBlock(BLOCK, UPGRADE_BLOCK);

        // Set the custom variable named "elementalBlock" with a base value and an upgrade value
        setCustomVar("elementalBlock", EXTRA_BLOCK, UPGRADE_EXTRA_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        com. badlogic. gdx. graphics. Color effectColor = Color.LIGHT_GRAY;

        int totalBlock = block;
        if (CardUtils.hasElementalPower()) {
            // Add the custom variable to the block total if the condition is met
            totalBlock += customVar("elementalBlock");
            effectColor = Color.GOLDENROD;
        }
        // Adding visual effect for gaining block
        addToBot(new VFXAction(new VerticalAuraEffect(effectColor,p.hb.cX, p.hb.cY)));

        addToBot(new GainBlockAction(p, p, totalBlock));
    }

    @Override
    public AbstractCard makeCopy() {
        return new EssenceBarrier();
    }
}
