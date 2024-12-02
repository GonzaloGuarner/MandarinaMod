package mandarinamod.cards.uncommon;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.GiantFireEffect;
import com.sun.org.apache.xpath.internal.functions.FuncFalse;
import mandarinamod.MandarinaMod;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.vfx.combat.EruptEffect;

public class Erupt extends BaseCard {
    public static final String ID = MandarinaMod.makeID(Erupt.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2
    );

    public Erupt() {
        super(ID, info);
        setDamage(3); // Base damage
        setMagic(3);
        initializeDescription();
    }
    private boolean UPGRADEDAMAGE = true;
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new VFXAction(new EruptEffect(magicNumber, damage)));
        for(int i = 0; i < this.magicNumber; ++i) {
            addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Erupt();
    }
    @Override
    public boolean canUpgrade(){
        return true;
    }
    @Override
    public void upgrade() {

            if (UPGRADEDAMAGE) {
                UPGRADEDAMAGE = false;
                upgradeDamage(1);
                initializeDescription();

            } else {
                UPGRADEDAMAGE = true;
                upgradeMagicNumber(1);
                initializeDescription();
            }
        this.upgraded = true;
        ++this.timesUpgraded;
        this.name = cardStrings.NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
    }
}
