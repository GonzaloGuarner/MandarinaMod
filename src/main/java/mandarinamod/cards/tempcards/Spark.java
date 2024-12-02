package mandarinamod.cards.tempcards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.util.CardStats;

public class Spark extends BaseCard {
    public static final String ID = MandarinaMod.makeID(Spark.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Load card strings
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,   // Card is colorless since it's a temporary card
            CardType.ATTACK,       // This is an attack card
            CardRarity.SPECIAL,    // Special rarity as it is a temporary card
            CardTarget.ALL_ENEMY,     // Targets a random enemy
            0                      // Costs 0 energy
    );

    private static final int DAMAGE = 4; // Base damage value
    private static final int UPGRADE_DAMAGE = 2;

    public Spark() {
        super(ID, info);
        setDamage(DAMAGE,UPGRADE_DAMAGE);
        setExhaust(true);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal damage to a random enemy
        addToBot(new DamageRandomEnemyAction(new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Spark();
    }
}
