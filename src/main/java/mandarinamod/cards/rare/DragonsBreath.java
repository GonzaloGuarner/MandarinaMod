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
    private static final int UPGRADED_DAMAGE = 2;
    private static final int UPGRADED_BURNT = 2;
    private static final int BURNT_THRESHOLD = 4;
    private static final int BURNT_THRESHOLD_UPGRADED = -1;

    private boolean upgradeBranchOne = true; // Tracks which branch is active

    public DragonsBreath() {
        super(ID, info);
        setDamage(BASE_DAMAGE);
        setMagic(BASE_BURNT); // Burnt applied
        setCustomVar("burntthreshold", BURNT_THRESHOLD, BURNT_THRESHOLD_UPGRADED);
        initializeDescription();

        if (CardCrawlGame.dungeon != null && AbstractDungeon.currMapNode != null) {
            this.configureCosts();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn))); // Deal damage
        addToBot(new ApplyPowerAction(m, p, new BurntPower(m, this.magicNumber), this.magicNumber)); // Apply Burnt

        if (this.upgraded && !upgradeBranchOne) { // Upgrade Path 2: Burns to Draw/Discard
            addToBot(new MakeTempCardInDrawPileAction(new Burn(), 1, true, true));
            addToBot(new MakeTempCardInDiscardAction(new Burn(), 1));
        } else { // Default behavior or Upgrade Path 1
            addToBot(new MakeTempCardInHandAction(new Burn()));
        }
    }


    @Override
    public void applyPowers() {
        super.applyPowers();
        if (CardCrawlGame.dungeon != null && AbstractDungeon.currMapNode != null) {
            this.configureCosts();
        }
    }


    public void configureCosts() {
        MonsterGroup monsterGroup = AbstractDungeon.getMonsters();
        if(monsterGroup == null) return;
        ArrayList<AbstractMonster> monsters = AbstractDungeon.getMonsters().monsters;
        if(monsters.isEmpty()) return;
        int totalBurnt = monsters.stream()
                .filter(monster -> !monster.isDeadOrEscaped())
                .mapToInt(monster -> monster.hasPower(BurntPower.POWER_ID)
                        ? monster.getPower(BurntPower.POWER_ID).amount
                        : 0)
                .sum();

        int modifiedCost = Math.max(0, this.baseCost - (totalBurnt / customVar("burntthreshold"))); // Reduce cost based on Burnt stacks
        if (this.baseCost != modifiedCost) {
            updateCost(modifiedCost-baseCost);
            if(this.cost != baseCost){
                this.isCostModified = true; // Mark that the cost is modified
            }
        }
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
        // Upgrade Path 2: Burns to Draw/Discard
        upgradeDamage(UPGRADED_DAMAGE);
        upgradeMagicNumber(UPGRADED_BURNT);
        this.rawDescription = cardStrings.DESCRIPTION;
        upgradeBranchOne = true;
        initializeDescription();

    }


    public void branchUpgrade() {
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        upgradeBranchOne = false;
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new DragonsBreath();
    }
}
