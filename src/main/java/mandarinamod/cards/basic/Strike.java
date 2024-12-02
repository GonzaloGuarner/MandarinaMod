package mandarinamod.cards.basic;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mandarinamod.cards.BaseCard;
import mandarinamod.character.Mandarina;
import mandarinamod.util.CardStats;
import mandarinamod.vfx.combat.ClawEffect;

public class Strike extends BaseCard {
    public static final String ID = makeID(Strike.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Mandarina.Meta.CARD_COLOR, // Specific card color for Mandarina
            CardType.ATTACK,
            CardRarity.BASIC, // Starting card for the character
            CardTarget.ENEMY,
            1 // Base energy cost
    );

    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 3;

    public Strike() {
        super(ID, info);

        setDamage(DAMAGE, UPG_DAMAGE); // Setting damage and upgrade damage

        tags.add(CardTags.STARTER_STRIKE);
        tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster monster) {
        ClawEffect.SlashDirection direction = Math.random() < 0.5 ? ClawEffect.SlashDirection.DIAGONAL_UP : ClawEffect.SlashDirection.DIAGONAL_DOWN;
        addToBot(new VFXAction(new ClawEffect(monster.hb.cX, monster.hb.cY, direction, Color.RED, Color.WHITE)));
        addToBot(new DamageAction(monster, new DamageInfo(abstractPlayer, damage, DamageInfo.DamageType.NORMAL)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Strike();
    }
}