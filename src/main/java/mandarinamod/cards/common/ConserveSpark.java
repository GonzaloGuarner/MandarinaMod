package mandarinamod.cards.common;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConservePower;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.cards.tempcards.Spark;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class ConserveSpark extends BaseCard implements BranchingUpgradesCard {
    public static final String ID = MandarinaMod.makeID(ConserveSpark.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            0 // Card cost
    );
    private static final int BLOCK = 2; // Base damage, increased to match Sucker Punch
    private static final int UPGRADE_BLOCK = 2; // Increase damage by 2 when upgraded
    private boolean upgradeBranchOne = true;

    public ConserveSpark() {
        super(ID, info);
        setBlock(BLOCK); // Base block and upgraded block
        setSelfRetain(true);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(upgradeBranchOne){
            addToBot(new GainBlockAction(p, this.block));
        }else{
            addToBot(new MakeTempCardInHandAction(new Spark()));
        }
        addToBot(new ApplyPowerAction(p, p, new ConservePower(p, 1))); // Apply Conserve for 1 turn
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            super.upgrade();
            if (isBranchUpgrade()) {
                branchUpgrade();
            } else {
                baseUpgrade();
            }
        }
    }
    public void baseUpgrade() {
        upgradeBlock(UPGRADE_BLOCK);
        this.rawDescription = cardStrings.DESCRIPTION;
        upgradeBranchOne = true;
        initializeDescription();

    }


    public void branchUpgrade() {
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        upgradeBranchOne = false;
        cardsToPreview = new Spark();
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new ConserveSpark();
    }
}

