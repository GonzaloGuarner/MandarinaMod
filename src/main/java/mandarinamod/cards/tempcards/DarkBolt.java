package mandarinamod.cards.tempcards;

import basemod.patches.com.megacrit.cardcrawl.screens.options.OptionsPanel.FixToggleHitboxSizeInRun;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;
import mandarinamod.util.ColorUtils;

public class DarkBolt extends BaseCard {
    public static final String ID = MandarinaMod.makeID(DarkBolt.class.getSimpleName());
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 // Cannot be played manually
    );

    public DarkBolt() {
        super(ID, info);
        setMagic(15, 5); // Base and upgraded damage are fixed at 15
        this.exhaust = true; // Automatically exhausts after use
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    }

//    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
//        //this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
//        return false;
//    }

    @Override
    public void triggerWhenDrawn() {
        flash(); // Provides visual feedback
        // Automatically deal damage to a random enemy
        AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(true);
        if (target != null) {
            addToBot(new DamageAction(target, new DamageInfo(AbstractDungeon.player, this.magicNumber, DamageInfo.DamageType.HP_LOSS)));
            AbstractDungeon.player.limbo.group.add(this);
            current_y = -200.0F * Settings.scale;
            target_x = (float)Settings.WIDTH / 2.0F + 200.0F * Settings.xScale;
            target_y = (float)Settings.HEIGHT / 2.0F;
            targetAngle = 0.0F;
            lighten(false);
            drawScale = 0.12F;
            targetDrawScale = 0.75F;
            applyPowers();
            this.addToTop(new NewQueueCardAction(this, target, false, true));
            this.addToTop(new UnlimboAction(this));
        }

    }

    @Override
    public AbstractCard makeCopy() {
        return new DarkBolt();
    }

    @Override
    public void triggerOnGlowCheck() {
        isGlowing = false;
        stopGlowing();
        super.triggerOnGlowCheck();
        //Default color

    }
}
