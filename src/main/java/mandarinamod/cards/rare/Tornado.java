package mandarinamod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import com.megacrit.cardcrawl.vfx.combat.WindyParticleEffect;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.FlowPower;
import mandarinamod.util.CardStats;

public class Tornado extends BaseCard {
    public static final String ID = MandarinaMod.makeID(Tornado.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Load card properties
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.ATTACK,           // This is an attack card
            CardRarity.RARE,           // Rare rarity
            CardTarget.ALL_ENEMY,      // Targets random enemies
            1                          // Costs 1 energy
    );

    private static final int BASE_DAMAGE = 2;       // Base damage per hit
    private static final int UPGRADE_DAMAGE = 1;    // Increase base damage by 1 when upgraded
    private static final int HITS = 5;              // Number of times to hit
    private static final int FLOW_DAMAGE = 2;       // Additional damage per Flow stack

    public Tornado() {
        super(ID, info);
        setDamage(BASE_DAMAGE, UPGRADE_DAMAGE);
        this.exhaust = true;                        // Card Exhausts after use
        this.rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Get the number of Flow stacks the player has
        int flowStacks = p.hasPower(FlowPower.POWER_ID) ? p.getPower(FlowPower.POWER_ID).amount : 0;

        // Calculate the total amount of hits
        int amountOfHits = HITS + flowStacks;

        // Deal damage to a random enemy 5 times
        for (int i = 0; i < amountOfHits; i++) {
            AbstractGameAction.AttackEffect attackEffect = (i % 3 == 0)
                    ? AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
                    : AbstractGameAction.AttackEffect.SLASH_VERTICAL;

//            if (i % 2 == 0) {
//                addToBot(new VFXAction(new WindyParticleEffect()));
//            }
            addToBot(new VFXAction(new WhirlwindEffect()));

            addToBot(new DamageRandomEnemyAction(
                    new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                    attackEffect));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);  // Increase base damage by 1 when upgraded
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Tornado();
    }
}
