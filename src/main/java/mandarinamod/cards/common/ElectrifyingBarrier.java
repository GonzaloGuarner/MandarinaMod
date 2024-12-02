package mandarinamod.cards.common;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.cards.tempcards.Spark;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.ElectrifyingBarrierPower;
import mandarinamod.util.CardStats;

public class ElectrifyingBarrier extends BaseCard {
    public static final String ID = MandarinaMod.makeID(ElectrifyingBarrier.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    private static final int BLOCK = 7;
    private static final int SPARK_TRIGGER_THRESHOLD = 10;       // Sparks generated per X damage blocked
    private static final int UPGRADE_TRIGGER_THRESHOLD = -3;

    public ElectrifyingBarrier() {
        super(ID, info);
        setBlock(BLOCK);
        setMagic(SPARK_TRIGGER_THRESHOLD,UPGRADE_TRIGGER_THRESHOLD);
        this.cardsToPreview = new Spark();  // Preview Spark card
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Gain initial Block and a single Spark
        this.addToBot(new GainBlockAction(p, p, this.block));

        // Apply Reactive Barrier power that tracks blocked damage
        this.addToBot(new ApplyPowerAction(p, p, new ElectrifyingBarrierPower(p, 1, this.magicNumber), 1));
    }


    @Override
    public AbstractCard makeCopy() {
        return new ElectrifyingBarrier();
    }
}
