package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.vfx.combat.FireballEffect;

public class Fireballs extends BaseCard {
    public static final String ID = MandarinaMod.makeID(Fireballs.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Load card strings
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.ATTACK,           // Card type
            CardRarity.UNCOMMON,           // Rarity
            CardTarget.ENEMY,      // Targets random enemies
            2                          // Costs 2 energy
    );

    private static final int DAMAGE = 5;           // Base damage per hit
    private static final int UPGRADE_PLUS_HIT = 1; // Upgrade gives one additional hit
    private static final int BASE_HITS = 3;        // Hits a random enemy 3 times initially

    public Fireballs() {
        super(ID, info);
        setDamage(DAMAGE);
        setMagic(BASE_HITS, UPGRADE_PLUS_HIT); // Magic number represents the number of hits
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Visual effect for fireballs
        for (int i = 0; i < this.magicNumber; i++) {
            addToBot(new VFXAction(new FireballEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY), 0.1F));
            addToBot(new DamageAction(m,
                    new DamageInfo(p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_HIT); // Increases the number of hits
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Fireballs();
    }
}
