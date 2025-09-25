package mandarinamod.cards.rare;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.PerfectPositionPower;
import mandarinamod.util.CardStats;

public class PerfectPosition extends BaseCard {
    public static final String ID = MandarinaMod.makeID(PerfectPosition.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            1
    );

    public PerfectPosition() {
        super(ID, info);
        setExhaust(true,false);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        // Apply the Perfect Position power
        addToBot(new ApplyPowerAction(player, player, new PerfectPositionPower(player,1)));
    }


    @Override
    public AbstractCard makeCopy() {
        return new PerfectPosition();
    }
}
