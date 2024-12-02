package mandarinamod.cards.pseudocards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.actions.GuessingGameAction;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.patches.Unmovable;
import mandarinamod.util.CardStats;
import mandarinamod.MandarinaMod;

public class SkillGuessChoice extends BaseCard implements Unmovable {
    public static final String ID = MandarinaMod.makeID(SkillGuessChoice.class.getSimpleName());
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2
    );

    public SkillGuessChoice() {
        super(ID, info);
        this.dontTriggerOnUseCard = true; // Prevent triggering actions like power checks
        this.purgeOnUse = true; // Card vanishes after use
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.onChoseThisOption();
    }

    public void onChoseThisOption() {
        AbstractDungeon.actionManager.addToBottom(new GuessingGameAction(CardType.SKILL));
    }

    @Override
    public BaseCard makeCopy() {
        return new SkillGuessChoice();
    }
}
