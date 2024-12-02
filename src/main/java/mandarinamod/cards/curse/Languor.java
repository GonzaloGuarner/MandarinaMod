package mandarinamod.cards.curse;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class Languor extends BaseCard {
    public static final String ID = MandarinaMod.makeID(Languor.class.getSimpleName());

    private static final CardStats info = new CardStats(
            CardColor.CURSE,
            CardType.CURSE,
            CardRarity.CURSE,
            CardTarget.NONE,
            1
    );

    public Languor() {
        super(ID, info);
        FleetingField.fleeting.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.addPower(new com.megacrit.cardcrawl.powers.StrengthPower(p, -1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Languor();
    }
}
