package mandarinamod.cards.pseudocards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import mandarinamod.actions.GuessingGameAction;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.patches.Unmovable;
import mandarinamod.util.CardStats;
import mandarinamod.MandarinaMod;

public class AttackGuessChoice extends BaseCard implements Unmovable {
    public static final String ID = MandarinaMod.makeID(AttackGuessChoice.class.getSimpleName());
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.ATTACK,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2
    );

    public AttackGuessChoice() {
        super(ID, info);
        this.dontTriggerOnUseCard = true; // Prevent triggering actions like power checks
        this.purgeOnUse = true; // Card vanishes after use
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.onChoseThisOption();
    }

    public void onChoseThisOption() {
        AbstractDungeon.actionManager.addToBottom(new GuessingGameAction(CardType.ATTACK));
    }

    @Override
    public BaseCard makeCopy() {
        return new AttackGuessChoice();
    }
}
