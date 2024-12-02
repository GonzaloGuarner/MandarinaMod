package mandarinamod.cards.rare;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hlysine.friendlymonsters.characters.AbstractPlayerWithMinions;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.monsters.DarklingMinion;
import mandarinamod.util.CardStats;

public class DarklingPact extends BaseCard {
    public static final String ID = MandarinaMod.makeID(DarklingPact.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.SKILL,            // Card type
            CardRarity.RARE,           // Rarity
            CardTarget.SELF,           // Targets self
            2                          // Costs 2 energy
    );

    // Darkling Pact constructor
    public DarklingPact() {
        super(ID, info);
        setMagic(6,-3); // Represents the HP loss
        setExhaust(true);
        initializeDescription();
    }


    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        if (player instanceof AbstractPlayerWithMinions) {
            AbstractDungeon.actionManager.addToBottom(new LoseHPAction(player, player, this.magicNumber));
            AbstractPlayerWithMinions p = (AbstractPlayerWithMinions) player;
            p.addMinion(new DarklingMinion(-650f, 0f, upgraded));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DarklingPact();
    }
}
