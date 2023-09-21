package com.patrick.zombiesarereal.entities;

import com.patrick.zombiesarereal.ai.ZombieAIEasternWander;
import com.patrick.zombiesarereal.ai.ZombieAIInvestigateSound;
import com.patrick.zombiesarereal.ai.ZombieAINearestAttackablePlayer;
import com.patrick.zombiesarereal.ai.ZombieAIWatchClosest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CustomBaseZombie extends EntityZombie
{
    public static final int    FOLLOW_RANGE                   = 32;
    public static final int    VISION_RANGE                   = 16;
    public static final int    TARGET_RANGE                   = 3;
    public static final double MIN_SPEED                      = 0.20D;
    public static final double MAX_SPEED                      = 0.40D;
    public static final double MAX_HEALTH                     = 10.0F;
    public static final double MIN_HEALTH                     = 1.0F;
    public static final double MIN_ATTACK_DAMAGE              = 4.0F;
    public static final double MAX_ATTACK_DAMAGE              = 20.0F;
    public static final double PASSIVE_SPEED_MULTIPLIER       = 0.4D;
    public static final double INVESTIGATION_SPEED_MULTIPLIER = 0.8D;

    public CustomBaseZombie(World worldIn)
    {
        super(worldIn);
        this.enablePersistence();
        this.setHealth(makeRandomHealth());
    }

    @Override
    public boolean isArmsRaised()
    {
        return false;
    }

    @Override
    protected void initEntityAI()
    {

        this.tasks.addTask(2, new EntityAIZombieAttack(this, 1.0D, false));
        this.targetTasks.addTask(2, new ZombieAINearestAttackablePlayer(this, EntityPlayer.class, true));
        this.tasks.addTask(3, new ZombieAIInvestigateSound(this, INVESTIGATION_SPEED_MULTIPLIER));
        this.tasks.addTask(6, new ZombieAIEasternWander(this, PASSIVE_SPEED_MULTIPLIER, 1));
        this.tasks.addTask(8, new ZombieAIWatchClosest(this, EntityPlayer.class, VISION_RANGE));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(FOLLOW_RANGE);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(makeRandomSpeed());
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(makeRandomAttackDamage());
    }

    @Override
    protected boolean shouldBurnInDay()
    {
        return false;
    }

    @Override
    protected ResourceLocation getLootTable()
    {
        return null;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn)
    {
        if (this.getDistance(entityIn) < 1.2)
        {
            return super.attackEntityAsMob(entityIn);
        }
        return false;
    }

    private float makeRandomHealth()
    {
        return (float) (MIN_HEALTH + (MAX_HEALTH - MIN_HEALTH) * rand.nextDouble());
    }

    private double makeRandomSpeed()
    {
        return MIN_SPEED + (MAX_SPEED - MIN_SPEED) * rand.nextDouble();
    }

    private double makeRandomAttackDamage()
    {
        return MIN_ATTACK_DAMAGE + (MAX_ATTACK_DAMAGE - MIN_ATTACK_DAMAGE) * rand.nextDouble();
    }
}
