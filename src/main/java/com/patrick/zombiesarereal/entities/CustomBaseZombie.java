package com.patrick.zombiesarereal.entities;

import com.patrick.zombiesarereal.ai.EntityAIEasternWander;
import com.patrick.zombiesarereal.ai.EntityAILookDown;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CustomBaseZombie extends EntityZombie
{
    public static final int    FOLLOW_RANGE             = 20;
    public static final double MIN_SPEED                = 0.25D;
    public static final double MAX_SPEED                = 0.45D;
    public static final double MAX_HEALTH               = 10.0F;
    public static final double MIN_HEALTH               = 1.0F;
    public static final double MIN_ATTACK_DAMAGE        = 1.0F;
    public static final double MAX_ATTACK_DAMAGE        = 6.0F;
    public static final double PASSIVE_SPEED_MULTIPLIER = 0.4D;

    public CustomBaseZombie(World worldIn)
    {
        super(worldIn);
        this.enablePersistence();
        this.setHealth(getRandomHealth());
    }

    @Override
    protected void initEntityAI()
    {
        this.tasks.addTask(2, new EntityAIZombieAttack(this, 1.0D, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, PASSIVE_SPEED_MULTIPLIER));
        this.tasks.addTask(5, new EntityAILookDown(this));
        this.tasks.addTask(6, new EntityAIEasternWander(this, PASSIVE_SPEED_MULTIPLIER, 1));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, PASSIVE_SPEED_MULTIPLIER));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, FOLLOW_RANGE));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(FOLLOW_RANGE);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(getRandomSpeed());
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(getRandomAttackDamage());
    }

    @Override
    protected boolean shouldBurnInDay()
    {
        return false;
    }

    public float getRandomHealth()
    {
        return (float) (MIN_HEALTH + (MAX_HEALTH - MIN_HEALTH) * rand.nextDouble());
    }

    public double getRandomSpeed()
    {
        return MIN_SPEED + (MAX_SPEED - MIN_SPEED) * rand.nextDouble();
    }

    public double getRandomAttackDamage()
    {
        return MIN_ATTACK_DAMAGE + (MAX_ATTACK_DAMAGE - MIN_ATTACK_DAMAGE) * rand.nextDouble();
    }
}
