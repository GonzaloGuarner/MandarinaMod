package mandarinamod.cards.uncommon;

import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import hlysine.friendlymonsters.characters.AbstractPlayerWithMinions;
import mandarinamod.actions.DamageBlockAction;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.FlowPower;
import mandarinamod.util.CardStats;
import mandarinamod.vfx.combat.BlockMassRemovalEffect;
import mandarinamod.vfx.combat.CrossCurrentsProjectileEffect;

public class CrossCurrents extends BaseCard {
    public static final String ID = makeID(CrossCurrents.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color
            CardType.ATTACK,           // Type
            CardRarity.UNCOMMON,       // Rarity
            CardTarget.ALL,      // Target
            0                          // Cost
    );

    private static final int DAMAGE = 8;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int BLOCK_REMOVE = 5;
    private static final int UPGRADE_BLOCK_REMOVE = 1;

    public CrossCurrents() {
        super(ID, info);
        setDamage(DAMAGE);
        setMagic(BLOCK_REMOVE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int targetsAffected  = 0;

        addToBot(new VFXAction(new BlockMassRemovalEffect(p.hb.cX, p.hb.cY)));

        if (p.currentBlock > 0) {
            targetsAffected++;
            addToBot(new DamageBlockAction(p, p, this.magicNumber));
        }

        if (AbstractDungeon.player instanceof AbstractPlayerWithMinions){
            MonsterGroup monstersGroup = ((AbstractPlayerWithMinions)AbstractDungeon.player).getMinions();
            if(!monstersGroup.monsters.isEmpty()){
                targetsAffected ++;
                addToBot(new DamageBlockAction(monstersGroup.monsters.get(0), p, this.magicNumber));
            }
        }

        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!mo.isDeadOrEscaped() && mo.currentBlock > 0) {
                targetsAffected ++;
                addToBot(new DamageBlockAction(mo, p, this.magicNumber));
            }
        }

        if (p.hasPower(FlowPower.POWER_ID)) {
            for (int i = 0; i < targetsAffected; i++) {
                AbstractMonster randomTarget = AbstractDungeon.getMonsters().getRandomMonster(true);
                if (randomTarget != null) {
                    addToBot(new VFXAction(new CrossCurrentsProjectileEffect(p.hb.cX, p.hb.cY, randomTarget, i%2)));
                }
                addToBot(new DamageAction(randomTarget, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeMagicNumber(UPGRADE_BLOCK_REMOVE);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CrossCurrents();
    }
}
