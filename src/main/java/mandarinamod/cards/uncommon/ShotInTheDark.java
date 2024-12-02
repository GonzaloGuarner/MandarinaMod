package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.cards.tempcards.DarkBolt;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

public class ShotInTheDark extends BaseCard {
    public static final String ID = MandarinaMod.makeID(ShotInTheDark.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1 // Card cost
    );

    public ShotInTheDark() {
        super(ID, info);
        setCostUpgrade(0);
        cardsToPreview = new DarkBolt();
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Shuffle a Dark Bolt into the draw pile
        AbstractCard darkBolt = new DarkBolt();
        addToBot(new MakeTempCardInDrawPileAction(darkBolt, 1, true, true));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShotInTheDark();
    }
}
