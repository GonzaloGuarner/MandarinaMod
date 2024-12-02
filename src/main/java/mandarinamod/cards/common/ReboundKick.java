package mandarinamod.cards.common;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class ReboundKick extends BaseCard {
    public static final String ID = MandarinaMod.makeID(ReboundKick.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1                    // Cost
    );

    private static final int DAMAGE = 8;
    private static final int UPGRADE_DAMAGE = 3;

    public ReboundKick() {
        super(ID, info);
        setDamage(DAMAGE, UPGRADE_DAMAGE);
        returnToHand = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        // Deal damage to the target
        addToBot(new DamageAction(monster, new DamageInfo(player, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
//        if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE && AbstractDungeon.player.discardPile.group.contains(this)) {
//            AbstractDungeon.player.hand.addToHand(this);
//            AbstractDungeon.player.discardPile.removeCard(this);
//        }
        this.cost = this.costForTurn = 1;
    }

    @Override
    public AbstractCard makeCopy() {
        return new ReboundKick();
    }
}
