package mandarinamod.cards.common;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.vfx.combat.ClashEffect;
import mandarinamod.MandarinaMod;
import mandarinamod.actions.ReduceFlowPowerAction;
import mandarinamod.cards.BaseCard;
import mandarinamod.util.CardStats;
import mandarinamod.character.Mandarina;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import mandarinamod.powers.FlowPower;

public class ShiftingCurrents extends BaseCard implements BranchingUpgradesCard {
    public static final String ID = MandarinaMod.makeID(ShiftingCurrents.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            0 // Cost
    );

    private static final int DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 2;

    public ShiftingCurrents() {
        super(ID, info);
        this.baseDamage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(FlowPower.POWER_ID) && p.getPower(FlowPower.POWER_ID).amount >= 2) {
            // Consume 2 Flow and deal double damage
            this.addToBot(new ReduceFlowPowerAction(p, p, FlowPower.POWER_ID, 2));
            if (upgraded && isBranchUpgrade()) {
                // If upgraded to branch path, deal double damage to all enemies
                this.addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(this.baseDamage * 2, true),
                        this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            } else {
                // Otherwise, deal double damage to the single enemy
                this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage * 2, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.NONE));
                this.addToBot(new VFXAction(new ClashEffect(m.hb.cX, m.hb.cY), 0.1F));
            }
        } else {
            // No Flow or less than 2 Flow, deal normal damage
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
    }

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

    public void baseUpgrade() {
        upgradeDamage(UPGRADE_DAMAGE); // Increase damage from 7 to 10
    }

    public void branchUpgrade() {
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION; // Update the description to reflect the new effect
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShiftingCurrents();
    }
}
