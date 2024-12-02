package mandarinamod.cards.common;

import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.CursedBlock;
import mandarinamod.util.CardStats;
//PENDING, COULD WORK ON THE CARD TARGETING TO BE SELF_OR_ENEMY ON A DOUBLE BRANCHING UPGRADE
public class SmogWave extends BaseCard {
    public static final String ID = MandarinaMod.makeID(SmogWave.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1 // Card cost
    );

    public SmogWave() {
        super(ID, info);
        setDamage(4, 2); // Base and upgraded damage
        setBlock(7, 2); // Base and upgraded block
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal damage
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL)));
        // Gain Cursed Block
        addToBot(new GainCustomBlockAction(this, p, this.block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SmogWave();
    }
}
