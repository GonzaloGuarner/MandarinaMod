package mandarinamod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PressurePointEffect;
import mandarinamod.MandarinaMod;
import mandarinamod.actions.TriggerPressureAction;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.powers.PressurePower;
import mandarinamod.util.CardStats;

public class FivePointStrike extends BaseCard {
    public static final String ID = MandarinaMod.makeID(FivePointStrike.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    public FivePointStrike() {
        super(ID, info);
        this.baseDamage = 10;
        this.magicNumber = this.baseMagicNumber = 4;  // Applies 0 Mark initially
        tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal 10 (14) damage to the targeted enemy
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

        if(upgraded){
            if (m != null) {
                this.addToBot(new VFXAction(new PressurePointEffect(m.hb.cX, m.hb.cY)));
            }
            addToBot(new ApplyPowerAction(m, p, new PressurePower(m, this.magicNumber), this.magicNumber));
            this.addToBot(new TriggerPressureAction(m));
        }
    }


    @Override
    public AbstractCard makeCopy() {
        return new FivePointStrike();
    }
}
