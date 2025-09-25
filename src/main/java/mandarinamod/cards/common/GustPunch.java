package mandarinamod.cards.common;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.cards.BaseCard;
import mandarinamod.powers.FlowPower;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import com.megacrit.cardcrawl.powers.WeakPower;

public class GustPunch extends BaseCard implements BranchingUpgradesCard {
    public static final String ID = makeID(GustPunch.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Specific card color for Mandarina
            CardType.ATTACK,           // Attack card
            CardRarity.COMMON,         // Common rarity
            CardTarget.ENEMY,          // Targets a single enemy
            1                          // Energy cost
    );

    private static final int DAMAGE = 7;             // Base damage
    private static final int UPGRADE_DAMAGE = 3;     // Increase damage by 2 when upgraded
    private static final int WEAK = 1;               // Amount of Weak applied
    private static final int UPGRADE_WEAK = 1;
    private static final int FLOW = 1;               // Amount of Flow gained

    private boolean upgradeBranchOne = true;

    public GustPunch() {
        super(ID, info);
        setDamage(DAMAGE);
        setMagic(WEAK);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal damage
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));

        // Apply Weak to the enemy
        addToBot(new ApplyPowerAction(
                m, p, new WeakPower(m, magicNumber, false), magicNumber,
                AbstractGameAction.AttackEffect.NONE));

        if (this.upgraded && !upgradeBranchOne) {
            int aliveEnemies = (int) AbstractDungeon.getMonsters().monsters.stream().filter(mon -> !mon.isDeadOrEscaped()).count();
            if (aliveEnemies > 0) {
                addToBot(new ApplyPowerAction(p, p, new FlowPower(p, aliveEnemies), aliveEnemies));
            }
        }
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
        upgradeMagicNumber(UPGRADE_WEAK);
        upgradeDamage(UPGRADE_DAMAGE);
        this.rawDescription = cardStrings.DESCRIPTION;
        upgradeBranchOne = true;
        initializeDescription();
    }

    public void branchUpgrade() {
//        upgradeDamage(UPGRADE_DAMAGE);
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        upgradeBranchOne = false;
        initializeDescription();
    }
    @Override
    public AbstractCard makeCopy() {
        return new GustPunch();
    }


}
