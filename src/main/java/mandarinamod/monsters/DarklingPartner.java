package mandarinamod.monsters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import mandarinamod.MandarinaMod;
import mandarinamod.powers.LifeLinkPower;

public class DarklingPartner extends PartnerMonster {
    public static final String MONSTER_ID = MandarinaMod.makeID(DarklingPartner.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(MONSTER_ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final int HP_MIN = 12;
    private static final int HP_MAX = 16;
    private static final int CHOMP_DMG = 3;
    private static final int UPGRADE_DMG = 5;
    private static final int BLOCK_AMT = 4;
    private Texture chompTexture = new Texture("mandarinamod/images/monsters/darkling/moves/atk_bubble.png");
    private Texture hardenTexture = new Texture("mandarinamod/images/monsters/darkling/moves/block_bubble.png");
    private Texture nipTexture = new Texture("mandarinamod/images/monsters/darkling/moves/sin_bubble.png");
    private boolean isReviving = false;
    private boolean renderCheckpoint = false;
    private boolean isUpgraded = false;
    private boolean isBlockMove = false;
    private AbstractMonster target;

    public DarklingPartner(float x, float y, boolean upgraded) {
        super(NAME, MONSTER_ID, MathUtils.random(HP_MIN, HP_MAX), 0.0F, 0,  upgraded? 250:200f,  upgraded? 200:200f, "mandarinamod/images/monsters/darkling/Darkling.png", x, y);
        this.isUpgraded = upgraded;
        this.scale = this.isUpgraded? 1f:0.5f;
        this.damage.add(new DamageInfo(this, this.isUpgraded?UPGRADE_DMG:CHOMP_DMG));
        //addMoves();
        //this.setMove((byte)1, Intent.ATTACK, 0, 2, true);
    }

    private void addMoves(){
//        moves.addMove(new MinionMove("Chomp", this, chompTexture, "Deal " + CHOMP_DMG + " damage twice.", () -> {
//            target = AbstractDungeon.getRandomMonster();
//            DamageInfo info = new DamageInfo(this, isUpgraded?UPGRADE_DMG:CHOMP_DMG, DamageInfo.DamageType.NORMAL);
//            //info.applyPowers(this, target); // <--- This lets powers effect minions attacks
//            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info));
//            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info));
//        }));
//        moves.addMove(new MinionMove("Harden", this, hardenTexture, "Gain " + BLOCK_AMT + " Block and apply Regrow.", () -> {
//            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this,  isUpgraded?BLOCK_AMT+2:BLOCK_AMT));
//         }));
//        moves.addMove(new MinionMove("Nip", this, nipTexture, "Deal " + NIP_DMG + " damage.", () -> {
//            target = AbstractDungeon.getRandomMonster();
//            DamageInfo info = new DamageInfo(this, NIP_DMG, DamageInfo.DamageType.NORMAL);
//            //info.applyPowers(this, target); // <--- This lets powers effect minions attacks
//            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//        }));
    }


    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new LifeLinkPower(this)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new LifeLinkPower(this)));
    }
    @Override
    public void applyStartOfTurnPowers() {
        super.applyStartOfTurnPowers();
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void applyEndOfTurnTriggers() {
        super.applyEndOfTurnTriggers();
    }
    @Override
    public void getMove(int num) {
        if (this.halfDead) {
            this.setMove((byte) 5, Intent.BUFF);
        }else{
            isBlockMove = MathUtils.randomBoolean();
            if(MathUtils.randomBoolean()){
                this.setMove((byte)2, Intent.DEFEND);
            }else{
                this.setMove((byte)3, Intent.ATTACK);
            }
        }
        this.createIntent();
    }
    public void takeTurn() {
        if(isBlockMove){
            target = AbstractDungeon.getRandomMonster();
            DamageInfo info = new DamageInfo(this, isUpgraded?UPGRADE_DMG:CHOMP_DMG, DamageInfo.DamageType.NORMAL);
            //info.applyPowers(this, target); // <--- This lets powers effect minions attacks
            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info));
        }else{
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this,  isUpgraded?BLOCK_AMT+2:BLOCK_AMT));
        }


        if(!halfDead) return;

        if(!isReviving){
            isReviving = true;
            AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this, DIALOG[0]));
        } else{
            if (MathUtils.randomBoolean()) {
                AbstractDungeon.actionManager.addToBottom(new SFXAction("DARKLING_REGROW_2", MathUtils.random(-0.1F, 0.1F)));
            } else {
                AbstractDungeon.actionManager.addToBottom(new SFXAction("DARKLING_REGROW_1", MathUtils.random(-0.1F, 0.1F)));
            }

            AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth / 2));
            isReviving = halfDead = false;
        }
    }


    @Override
    public void render(SpriteBatch sb) {
        if (!renderCheckpoint) {
            System.out.println(" render method reached for the first time");
            renderCheckpoint = true;
        }
        super.render(sb);
    }
    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead) {
            this.halfDead = true;
        }
    }
    @Override
    public void die() {
        if (AbstractDungeon.player.halfDead || !this.hasPower(LifeLinkPower.POWER_ID))
        {
            super.die();
            //AbstractDungeon.player.damage(new DamageInfo(this,0));
        }
    }
}
