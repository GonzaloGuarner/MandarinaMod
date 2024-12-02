package mandarinamod.cards.uncommon;

import com.gikk.twirk.SETTINGS;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.actions.watcher.TriggerMarksAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.actions.PressureReliefAction;
import mandarinamod.actions.TriggerPressuresAction;
import mandarinamod.cards.BaseCard;
import com.megacrit.cardcrawl.powers.watcher.MarkPower;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.PressurePower;
import mandarinamod.util.CardStats;

public class PressureRelief extends BaseCard {
    public static final String ID = MandarinaMod.makeID(PressureRelief.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2
    );

    public PressureRelief() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = 7;  // Applies 7 Mark initially
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            // Add a new WaitForAction to manage waiting for the TriggerPressuresAction to complete
            addToBot(new PressureReliefAction(m, p, magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(3);  // Increase Mark application to 10 when upgraded
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PressureRelief();
    }
}
