package mandarinamod.cards.rare;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.util.CardUtils;
import mandarinamod.util.ColorUtils;
import mandarinamod.util.CustomTags;

import javax.smartcardio.Card;

public class MagneticSiphon extends BaseCard {
    public static final String ID = MandarinaMod.makeID(MagneticSiphon.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.ENEMY,
            2 // Card cost
    );

    private static final int GOLD_GAIN = 15;
    private static final int GOLD_GAIN_UPGRADE = 5;
    private static final int DAMAGE = 25;
    private static final int GOLD_COST = 25;

    public MagneticSiphon() {
        super(ID, info);
        setMagic(GOLD_GAIN, GOLD_GAIN_UPGRADE); // Gold gain (base and upgraded)
        this.exhaust = true; // Card exhausts after use
        initializeDescription();
        tags.add(CustomTags.ODD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainGoldAction(this.magicNumber)); // Gain gold
        for(int i = 0; i < this.magicNumber; ++i) {
            AbstractDungeon.effectList.add(new GainPennyEffect(p, m.hb.cX, m.hb.cY, p.hb.cX, p.hb.cY, true));
        }
        if (CardUtils.isOddPosition() || CardUtils.isPerfectPosition()) { // Check if played odd
            if (AbstractDungeon.player.gold >= GOLD_COST) {
                AbstractDungeon.player.loseGold(GOLD_COST); // Spend gold
                addToBot(new LoseHPAction(m, p, DAMAGE));
                //this.addToBot(new ThrowGoldEffect(AbstractDungeon.player, this, 5, false));
            } else {
                CardCrawlGame.sound.play("UI_CLICK"); // Indicate insufficient gold
            }
        }
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
        if (CardUtils.isPerfectPosition()) {
            // Set the glow color to gold
            this.glowColor = ColorUtils.GOLD_GLOW.cpy();
        }
    }
    @Override
    public AbstractCard makeCopy() {
        return new MagneticSiphon();
    }
}
