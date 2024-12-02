package mandarinamod.powers;


import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnPlayerDeathPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import hlysine.friendlymonsters.utils.MonsterIntentUtils;
import mandarinamod.MandarinaMod;

public class LifeLinkPower extends BasePower implements OnPlayerDeathPower, NonStackablePower {
        public static final String POWER_ID = MandarinaMod.makeID(LifeLinkPower.class.getSimpleName());
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private boolean isPlayerDead = false;
    private boolean isPlayerReviving = false;

        public LifeLinkPower(AbstractCreature owner) {
            super(POWER_ID, PowerType.BUFF, false, owner, null, 0, true, false);
            this.loadRegion("regrow");
            this.name = NAME;
            this.updateDescription();
        }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public boolean onPlayerDeath(AbstractPlayer abstractPlayer, DamageInfo damageInfo) {
        isPlayerDead = true;
        if (this.owner.hasPower(POWER_ID) && !this.owner.halfDead) {
            //AbstractDungeon.actionManager.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 2));
            RevivePlayer();
            redirectIntent();
            return false;
        }

        redirectIntent();

        return true;
    }
    @Override
    public void atStartOfTurn(){
//        if(isPlayerReviving)
//        {
//            RevivePlayer();
//            redirectIntent();
//        } else if (isPlayerDead) {
//            //AbstractDungeon.actionManager.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 1));
//            isPlayerReviving = true;
//            redirectIntent();
//        }
    }
    @Override
    public void atEndOfTurn(boolean isPlayer){
//            if (isPlayer && isPlayerDead  ){//hasPower(POWER_ID)
//                RevivePlayer();
//            }
    }

    void RevivePlayer(){
        AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(AbstractDungeon.player, this.owner, LifeLinkPower.POWER_ID));
        AbstractDungeon.actionManager.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 10));
        AbstractDungeon.actionManager.addToTop(new VFXAction(this.owner, new IntenseZoomEffect(this.owner.hb.cX, this.owner.hb.cY, true), 0.05F, true));
        AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, LifeLinkPower.POWER_ID));
        isPlayerDead = isPlayerReviving = false;
    }

    void redirectIntent(){
        if(this.owner instanceof AbstractFriendlyMonster){
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                MonsterIntentUtils.setTarget(monster, (AbstractFriendlyMonster)this.owner);
            }
        }
    }

    static {
            powerStrings = CardCrawlGame.languagePack.getPowerStrings(LifeLinkPower.class.getSimpleName());
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }



}
