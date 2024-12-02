package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;

import java.util.HashSet;
import java.util.Set;

public class UnbalancingWinds extends BaseCard {
    public static final String ID = makeID(UnbalancingWinds.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Load card strings
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Card color for Mandarina
            CardType.ATTACK,           // Card type
            CardRarity.UNCOMMON,       // Rarity
            CardTarget.ALL_ENEMY,      // Targets all enemies (randomly)
            2                          // Costs 2 energy
    );

    private static final int DAMAGE = 7;               // Damage per hit (lowered from 7)
    private static final int UPGRADE_DAMAGE = 1;       // Upgrade: Increase damage per hit by 2
    private static final int VULNERABLE_DURATION = 2;
    private static final int VULNERABLE_UPGRADE = 1;// Weak duration if both hits land on the same target

    public UnbalancingWinds() {
        super(ID, info);
        setDamage(DAMAGE, UPGRADE_DAMAGE);
        setMagic(VULNERABLE_DURATION,VULNERABLE_UPGRADE);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Randomly deal damage twice
        Set<AbstractMonster> targetsHit = new HashSet<>();

        for (int i = 0; i < 2; i++) {
            AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            if (randomMonster != null) {
                targetsHit.add(randomMonster);
                addToBot(new VFXAction(new FlashAtkImgEffect(
                        randomMonster.hb.cX, randomMonster.hb.cY, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)));
                addToBot(new DamageAction(
                        randomMonster,
                        new DamageInfo(p, this.damage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.NONE));
            }
            // Small wait to separate the animations
            addToBot(new WaitAction(0.1f));
        }

        // If both hits landed on the same target, apply the extra effect (Weak)
        if (targetsHit.size() == 1) {
            AbstractMonster target = targetsHit.iterator().next();
            addToBot(new ApplyPowerAction(
                    target, p, new VulnerablePower(target, VULNERABLE_DURATION, false), VULNERABLE_DURATION));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new UnbalancingWinds();
    }

//    @Override
//    public void upgrade() {
//        if (!upgraded) {
//            upgradeName();
//            upgradeDamage(UPGRADE_DAMAGE);
//            upgradeDamage(UPGRADE_DAMAGE);
//            this.rawDescription = UPGRADE_DESCRIPTION;
//            initializeDescription();
//        }
//    }
}
