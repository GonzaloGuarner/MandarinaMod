package mandarinamod.cards.rare;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.FlowPower;
import mandarinamod.powers.FlowStatePower;
import mandarinamod.util.CardStats;

public class FlowState extends BaseCard implements BranchingUpgradesCard {
    public static final String ID = MandarinaMod.makeID(FlowState.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            2 // Card cost
    );
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int BASE_DAMAGE = 3;
    private static final int DAMAGE_UPGRADE_1 = 2;
    private static final int DAMAGE_UPGRADE_2 = 2;
    private static final int BASE_FLOW_GAIN = 0;
    private static final int FLOW_UPGRADE_1 = 3;
    private static final int FLOW_UPGRADE_2 = 3;



    public FlowState() {
        super(ID, info);
        setDamage(BASE_DAMAGE);
        setMagic(0); // Base damage dealt per flow gain/loss
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new FlowStatePower(p, this.damage)));
        if(this.magicNumber > 0){
            addToBot(new ApplyPowerAction(p, p, new FlowPower(p, this.magicNumber)));
        }
    }



//    @Override
//    public boolean canUpgrade() {
//        return false;
////        boolean one = this.magicNumber == BASE_DAMAGE+DAMAGE_UPGRADE_1 && this.damage == BASE_DAMAGE+DAMAGE_UPGRADE_1;
////        boolean two = this.damage == BASE_DAMAGE+DAMAGE_UPGRADE_1+DAMAGE_UPGRADE_2;
////        boolean three = this.magicNumber == BASE_FLOW_GAIN+FLOW_UPGRADE_1+FLOW_UPGRADE_2;
//        //return !(one||two||three);
//    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            if (isBranchUpgrade()) {
                branchUpgrade();
            } else {
                baseUpgrade();
            }
        }
    }
//    @Override
//    public void upgrade() {
//            //upgradeName();
//            if (isBranchUpgrade()) {
//                branchUpgrade();
//            } else {
//                baseUpgrade();
//            }
//
//            //upgradeName();
//
//            //this.name = cardStrings.NAME + "+" + (upgradedDamageBranch+upgradedFlowBranch);
////           this.upgraded = true;
////            ++this.timesUpgraded;
////            this.name = cardStrings.NAME + "+" + this.timesUpgraded;
//            //initializeTitle();
////        initializeDescription();
//
//    }

    public void baseUpgrade() {
        // Upgrade the damage
        upgradeDamage(DAMAGE_UPGRADE_1); // First damage upgrade
        initializeDescription();
    }

    public void branchUpgrade() {
        // Upgrade the flow gain
        this.rawDescription = UPGRADE_DESCRIPTION;
        upgradeMagicNumber(FLOW_UPGRADE_1); // First flow upgrade
        initializeDescription();
    }

//    @Override
//    public void upgrade() {
////        if (!this.upgraded) {
////            upgradeName();
////        }
//
//        if (isBranchUpgrade()) {
//            doBranchUpgrade();
//        } else {
//            doNormalUpgrade();
//        }
//
//        this.upgraded = true;
//        ++this.timesUpgraded;
//        this.name = cardStrings.NAME + "+" + this.timesUpgraded;
//        initializeDescription();
//    }
//
//    @Override
//    public void doBranchUpgrade() {
//        // Upgrade the flow gain
//        this.rawDescription = UPGRADE_DESCRIPTION;
//        if (this.magicNumber == BASE_FLOW_GAIN) {
//            upgradeMagicNumber(FLOW_UPGRADE_1); // First flow upgrade
//            initializeDescription();
//        } else {
//            upgradeMagicNumber(FLOW_UPGRADE_2); // Second flow upgrade
//            initializeDescription();
//        }
//        upgradedFlowBranch = true;
//    }
//
//    @Override
//    public void doNormalUpgrade() {
//        // Upgrade the damage
//
//        if (this.damage == BASE_DAMAGE) {
//            upgradeDamage(DAMAGE_UPGRADE_1); // First damage upgrade
//            initializeDescription();
//        } else {
//            upgradeDamage(DAMAGE_UPGRADE_2); // Second damage upgrade
//            initializeDescription();
//        }
//    }

    @Override
    public AbstractCard makeCopy() {
        return new FlowState();
    }
}

