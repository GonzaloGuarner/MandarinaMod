package mandarinamod.cards.common;

import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModContainer;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.CursedBlock;
import mandarinamod.util.CardStats;
//PENDING, COULD WORK ON THE CARD TARGETING TO BE SELF_OR_ENEMY ON A DOUBLE BRANCHING UPGRADE
public class SmogBall extends BaseCard {
    public static final String ID = MandarinaMod.makeID(SmogBall.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            SelfOrEnemyTargeting.SELF_OR_ENEMY,
            1 // Card cost
    );

    public SmogBall() {
        super(ID, info);
        setDamage(4, 2); // Base and upgraded damage
        setBlock(7, 2); // Base and upgraded block
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCreature target = SelfOrEnemyTargeting.getTarget(this);
        if (target == null)
            target = AbstractDungeon.player;

        addToBot(new GainCustomBlockAction(new BlockModContainer(this, new CursedBlock()), target, this.block));

        if (target == AbstractDungeon.player) {
            // Choose a random alive monster in the room
            target = AbstractDungeon.getMonsters().getRandomMonster(true);
        }

        // Deal damage
        addToBot(new DamageAction(target, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.POISON));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SmogBall();
    }
}
