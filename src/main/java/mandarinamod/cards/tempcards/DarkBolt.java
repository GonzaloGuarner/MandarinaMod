package mandarinamod.cards.tempcards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.util.CardStats;

public class DarkBolt extends BaseCard {
    public static final String ID = MandarinaMod.makeID(DarkBolt.class.getSimpleName());
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.ENEMY,
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

    @Override
    public void triggerWhenDrawn() {
        flash(); // Provides visual feedback
        // Automatically deal damage to a random enemy
        AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(true);
        if (target != null) {
            addToBot(new DamageAction(target, new DamageInfo(AbstractDungeon.player, this.magicNumber, DamageInfo.DamageType.HP_LOSS)));
        }
        addToBot(new ExhaustAction(1, false)); // Exhaust this card after triggering
    }

    @Override
    public AbstractCard makeCopy() {
        return new DarkBolt();
    }
}
