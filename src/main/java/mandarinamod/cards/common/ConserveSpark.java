package mandarinamod.cards.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConservePower;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class ConserveSpark extends BaseCard {
    public static final String ID = MandarinaMod.makeID(ConserveSpark.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            0 // Card cost
    );

    public ConserveSpark() {
        super(ID, info);
        setBlock(2, 2); // Base block and upgraded block
        this.retain = true; // Retain mechanic
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, this.block));
        addToBot(new ApplyPowerAction(p, p, new ConservePower(p, 1))); // Apply Conserve for 1 turn
    }

    @Override
    public AbstractCard makeCopy() {
        return new ConserveSpark();
    }
}

