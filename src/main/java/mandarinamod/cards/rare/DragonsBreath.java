package mandarinamod.cards.rare;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import mandarinamod.MandarinaMod;
import mandarinamod.actions.DragonsBreathCostAction;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.BurntPower;
import mandarinamod.util.CardStats;

import java.util.ArrayList;

public class DragonsBreath extends BaseCard implements BranchingUpgradesCard {
    public static final String ID = MandarinaMod.makeID(DragonsBreath.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            3 // Base cost
    );

    private static final int BASE_DAMAGE = 8;
    private static final int BASE_BURNT = 8;
    private static final int UPGRADED_DAMAGE = 0;
    private static final int UPGRADED_BURNT = 2;
    private static final int UPGRADED_DAMAGE2 = 0;
    private static final int UPGRADED_BURNT2 = 5;
    private static final int BURNT_THRESHOLD = 3;
    private static final int BURNT_THRESHOLD_UPGRADED = -1;

    private boolean upgradeBranchOne = true; // Tracks which branch is active

    public DragonsBreath() {
        super(ID, info);
        setDamage(BASE_DAMAGE);
        setMagic(BASE_BURNT); // Burnt applied
        setCustomVar("burntthreshold", BURNT_THRESHOLD, BURNT_THRESHOLD_UPGRADED);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL)));
        addToBot(new ApplyPowerAction(m, p, new BurntPower(m, magicNumber), magicNumber));
        addToBot(new MakeTempCardInDiscardAction(new Burn(), 1));
        if (this.upgraded && !upgradeBranchOne) { // Upgrade Path 2: Burns to Draw+Discard
            addToBot(new MakeTempCardInDrawPileAction(new Burn(), 1, true, true));
        }
    }


    @Override
    public void atTurnStart() {
        configureCosts();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        configureCosts();
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        configureCosts();
    }
    public void configureCosts() {
        addToBot(new DragonsBreathCostAction(this));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            super.upgrade();
            //upgradeName();
            if (isBranchUpgrade()) {
                branchUpgrade(); // Upgrade Path 1
            } else {
                baseUpgrade(); // Upgrade Path 2
            }
        }
    }


    public void baseUpgrade() {
        //upgradeDamage(UPGRADED_DAMAGE);
        upgradeMagicNumber(UPGRADED_BURNT);
        this.rawDescription = cardStrings.DESCRIPTION;
        upgradeBranchOne = true;
        initializeDescription();

    }


    public void branchUpgrade() {
        //upgradeDamage(UPGRADED_DAMAGE2);
        upgradeMagicNumber(UPGRADED_BURNT2);
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        upgradeBranchOne = false;
        initializeDescription();
    }

    public int BaseCost() {
        return this.baseCost;
    }

    @Override
    public AbstractCard makeCopy() {
        return new DragonsBreath();
    }
}
