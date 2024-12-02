package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;
import mandarinamod.util.ColorUtils;

public class Hunger extends BaseCard {
    public static final String ID = MandarinaMod.makeID(Hunger.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color
            CardType.ATTACK,             // Card type
            CardRarity.UNCOMMON,         // Rarity
            CardTarget.ENEMY,            // Target
            2                            // Cost
    );

    private static final int DAMAGE = 14;
    private static final int UPGRADE_DAMAGE = 5;

    public Hunger() {
        super(ID, info);
        setDamage(DAMAGE, UPGRADE_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        if (monster != null) {
            if (Settings.FAST_MODE) {
                this.addToBot(new VFXAction(new BiteEffect(monster.hb.cX, monster.hb.cY - 40.0F * Settings.scale, Settings.PURPLE_COLOR.cpy()), 0.1F));
            } else {
                this.addToBot(new VFXAction(new BiteEffect(monster.hb.cX, monster.hb.cY - 40.0F * Settings.scale, Settings.PURPLE_COLOR.cpy()), 0.3F));
            }
        }

        this.addToBot(new DamageAction(monster, new DamageInfo(player, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));

        // Positional bonus: Odd or Even
        if (CardUtils.isOddPosition() || CardUtils.isPerfectPosition()) {
            addToBot(new DrawCardAction(player, 2)); // Immediate draw
        }
        if (CardUtils.isEvenPosition() || CardUtils.isPerfectPosition()) {
            addToBot(new ApplyPowerAction(player, player, new DrawCardNextTurnPower(player, 2), 2));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Hunger();
    }
    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        //Default color
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

        // Odd amount of cards played before
        if (CardUtils.isOddPosition()) {
            // Set the glow color to magenta if card would be even
            this.glowColor = ColorUtils.MAGENTA_GLOW.cpy();
        }
        if (CardUtils.isEvenPosition()) {
            // Set the glow color to green if card would be odd
            this.glowColor = ColorUtils.GREEN_GLOW.cpy();
        }
        if (CardUtils.isPerfectPosition()) {
            // Set the glow color to gold
            this.glowColor = ColorUtils.GOLD_GLOW.cpy();
        }
    }
}
