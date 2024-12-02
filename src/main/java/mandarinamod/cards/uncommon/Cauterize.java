package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class Cauterize extends BaseCard {
    public static final String ID = MandarinaMod.makeID(Cauterize.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0 // Card cost
    );

    public Cauterize() {
        super(ID, info);
        setMagic(6, 2); // Base and upgraded heal amount
        setExhaust(true);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new HealAction(p, p, this.magicNumber)); // Heal
        addToBot(new MakeTempCardInHandAction(new Burn())); // Add a Burn card to hand
    }

    @Override
    public AbstractCard makeCopy() {
        return new Cauterize();
    }
}
