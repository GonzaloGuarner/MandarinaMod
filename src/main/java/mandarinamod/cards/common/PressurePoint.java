package mandarinamod.cards.common;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PressurePointEffect;
import mandarinamod.MandarinaMod;
import mandarinamod.actions.TriggerPressuresAction;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.PressurePower;
import mandarinamod.util.CardStats;

public class PressurePoint extends BaseCard {
    public static final String ID = MandarinaMod.makeID(PressurePoint.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    public PressurePoint() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = 6;  // Applies 6 Mark initially
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            this.addToBot(new VFXAction(new PressurePointEffect(m.hb.cX, m.hb.cY)));
        }

        // Apply 6 (9) Mark to the targeted enemy
        addToBot(new ApplyPowerAction(m, p, new PressurePower(m, this.magicNumber), this.magicNumber));

        // Trigger all Mark effects on all enemies
        addToBot(new TriggerPressuresAction());
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(3);  // Increase Mark application to 9 upon upgrade
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PressurePoint();
    }
}
