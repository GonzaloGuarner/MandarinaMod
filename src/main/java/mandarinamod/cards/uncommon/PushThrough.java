package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.WallopEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;

public class PushThrough extends BaseCard {
    public static final String ID = MandarinaMod.makeID(PushThrough.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1                    // Cost
    );

    private static final int DAMAGE = 8;
    private static final int UPGRADE_DAMAGE = 3;
    public boolean repeatable = true;

    public PushThrough() {
        super(ID, info);
        setDamage(DAMAGE, UPGRADE_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        int finalDamage = this.damage;

        // Check if the enemy has Block
        if (monster.currentBlock > 0 && repeatable) {
            playThisAgain((monster)); // Double damage if the enemy has Block
        }

        // Deal damage
        addToBot(new DamageAction(monster, new DamageInfo(player, finalDamage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        AbstractDungeon.effectList.add(new FlashAtkImgEffect(monster.hb.cX, monster.hb.cY, AbstractGameAction.AttackEffect.BLUNT_HEAVY, false));
        this.addToBot(new VFXAction(new WallopEffect(finalDamage, monster.hb.cX, monster.hb.cY)));
    }


    void playThisAgain(AbstractMonster m){
        PushThrough tmp = (PushThrough)this.makeSameInstanceOf();
        tmp.setRepeatable(false);
        AbstractDungeon.player.limbo.addToBottom(tmp);
        tmp.current_x = this.current_x;
        tmp.current_y = this.current_y;
        tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        tmp.target_y = (float)Settings.HEIGHT / 2.0F;
        if (m != null) {
            tmp.calculateCardDamage(m);
        }

        tmp.purgeOnUse = true;
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, this.energyOnUse, true, true), true);
    }

    public void setRepeatable(boolean repeatable){
        this.repeatable = repeatable;
    }

    @Override
    public AbstractCard makeCopy() {
        return new PushThrough();
    }
}
