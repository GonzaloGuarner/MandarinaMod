package mandarinamod.cards.uncommon;

import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModContainer;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import hlysine.friendlymonsters.characters.AbstractPlayerWithMinions;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.CursedBlock;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;
import mandarinamod.util.ColorUtils;
import mandarinamod.util.CustomTags;

public class OddSmokes extends BaseCard {
    public static final String ID = MandarinaMod.makeID(OddSmokes.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            1
    );

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final int BLOCK = 6;
    private static final int UPGRADE_BLOCK = 2;
    private static final int SMOKE_BLOCK = 4;
    private static final int SMOKE_BLOCK_UPG = 2;

    public OddSmokes() {
        super(ID, info);
        setBlock(BLOCK, UPGRADE_BLOCK);
        setMagic(SMOKE_BLOCK, SMOKE_BLOCK_UPG);
        tags.add(CustomTags.ODD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        // Check if the card is played in an odd position
        if (CardUtils.isOddPosition() || CardUtils.isPerfectPosition()) { // Odd position: 1st, 3rd, 5th, etc.
            BlockModContainer cursed = new BlockModContainer(this, new CursedBlock());
            if (AbstractDungeon.player instanceof AbstractPlayerWithMinions){
                MonsterGroup monstersGroup = ((AbstractPlayerWithMinions)AbstractDungeon.player).getMinions();
                if(!monstersGroup.monsters.isEmpty()){
                    addToBot(new GainCustomBlockAction(cursed, monstersGroup.monsters.get(0),  this.magicNumber));
                }
            }

            AbstractDungeon.getMonsters().monsters.forEach(monster -> {
                addToBot(new GainCustomBlockAction(cursed, monster, this.magicNumber)); // Enemies gain block
            });
            addToBot(new GainCustomBlockAction(cursed, AbstractDungeon.player, this.magicNumber));
        }

        addToBot(new GainBlockAction(AbstractDungeon.player, this.block));
    }
    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        //Default color
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

        // Even amount of cards played before
        if (CardUtils.isEvenPosition()) {
            // Set the glow color to green if card would be odd
            this.glowColor = ColorUtils.GREEN_GLOW.cpy();
        }
        if (CardUtils.isPerfectPosition()) {
            // Set the glow color to gold
            this.glowColor = ColorUtils.GOLD_GLOW.cpy();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new OddSmokes();
    }
}
