package mandarinamod.cards.rare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.WallopEffect;
import com.megacrit.cardcrawl.vfx.combat.GiantFireEffect;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;
import mandarinamod.util.ColorUtils;
import mandarinamod.vfx.combat.ClawEffect;

import javax.swing.plaf.ColorUIResource;

public class PositionalStrike extends BaseCard {
    public static final String ID = MandarinaMod.makeID(PositionalStrike.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,  // Adjust color as needed
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            1               // Cost
    );

    private static final int DAMAGE = 5;
    private static final int UPGRADE_DAMAGE = 0;
    private static final int EXTRA_DAMAGE = 3;
    private static final int UPGRADE_EXTRA_DAMAGE = 3;
    private float extra_damage_ratio = 0.6f; // Changing this values should mean changing damage above acc.
    private float extra_damage_upgraded_ratio = 1.2f;

    public PositionalStrike() {
        super(ID, info);
        setDamage(DAMAGE, UPGRADE_DAMAGE);
        setMagic(EXTRA_DAMAGE,UPGRADE_EXTRA_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        int finalDamage = this.damage;
        AbstractGameEffect effect = new ClawEffect(monster.hb.cX, monster.hb.cY,  ClawEffect.SlashDirection.DIAGONAL_UP , Color.RED, Color.WHITE);
        // Check if the card is played in an Odd position
        if (CardUtils.isOddPosition() || CardUtils.isPerfectPosition()) {
            finalDamage += this.magicNumber;
            effect = new ClawEffect(monster.hb.cX, monster.hb.cY,  ClawEffect.SlashDirection.DIAGONAL_DOWN , Color.RED, Color.WHITE);
        }

        if (CardUtils.isThirdPosition() || CardUtils.isPerfectPosition()) {
            finalDamage *= 2;
            effect = new ClawEffect(monster.hb.cX, monster.hb.cY,  ClawEffect.SlashDirection.DOWN , Color.RED, Color.WHITE);
        }
        this.addToBot(new VFXAction(effect));
        // Apply damage
        addToBot(new DamageAction(monster, new DamageInfo(player, finalDamage, damageTypeForTurn)));

    }



    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);
        if(CardUtils.isEvenPosition() || CardUtils.isPerfectPosition()){
            this.damage += this.magicNumber;
        }
        if(CardUtils.isNPosition(2) || CardUtils.isPerfectPosition()){
            this.damage *= 2;
        }

        isDamageModified = this.damage != baseDamage;
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();

        //Default color
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

        // Even amount of cards played before
        if (CardUtils.isEvenPosition()) {
            // Set the glow color to gold if card would be odd
            this.glowColor = ColorUtils.GREEN_GLOW.cpy();
        }
        if (CardUtils.isNPosition(2) || CardUtils.isPerfectPosition()) {
            // Glow color is gold if card would be third
            this.glowColor = ColorUtils.GOLD_GLOW.cpy();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PositionalStrike();
    }
}

