package mandarinamod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.patches.EnumPatch;
import mandarinamod.util.CardStats;
import mandarinamod.vfx.combat.AuroraRayEffect;


public class AuroraRay extends BaseCard {
    public static final String ID = makeID(AuroraRay.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.ATTACK,           // This is an attack card
            CardRarity.RARE,           // Rare card rarity
            CardTarget.ENEMY,          // Targets a single enemy
            5                          // Initial cost is 6 energy
    );

    private static final int DAMAGE = 52;          // Base damage value
    private static final int UPGRADE_DAMAGE = 0;  // Damage increase when upgraded
    private static final int UPGRADE_COST = 4;    // Reduced cost when upgraded

    public AuroraRay() {
        super(ID, info);
        setDamage(DAMAGE, UPGRADE_DAMAGE);
        setSelfRetain(true); // Card will be retained in the player's hand at the end of the turn
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Visual effect for a lot of lightning
        if (m == null) {
            return;
        }
//            this.addToBot(new VFXAction(new WeightyImpactEffect(m.hb.cX, m.hb.cY)));
//            for (int i = 0; i < 3; i++) { // Adding multiple lightning effects for a dramatic impact
//                float offsetX = (i - 1) * 10f;
//                AbstractDungeon.effectList.add(new LightningEffect(m.hb.cX + offsetX, m.hb.cY));
//
        this.addToBot(new VFXAction(new AuroraRayEffect(m.hb.cX, m.hb.cY)));
        this.baseCost = upgraded ? UPGRADE_COST : UPGRADE_COST+1;


        // Deal massive single-target damage
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        // Reduce the card cost by unspent energy at the end of the player's turn
        int unspentEnergy = EnergyPanel.totalCount;
        if (unspentEnergy > 0) {
            this.updateCost(-unspentEnergy);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE); // Upgrade damage from 66 to 75
            upgradeBaseCost(UPGRADE_COST); // Reduce cost from 6 to 5
            initializeDescription();
        }
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();
        // Ensure cost doesn't go below 0 after reduction
        if (this.cost < 0) {
            this.cost = 0;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new AuroraRay();
    }
}
