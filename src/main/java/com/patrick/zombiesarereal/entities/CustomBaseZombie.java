package com.patrick.zombiesarereal.entities;

import com.patrick.zombiesarereal.ai.EntityAIEasternWander;
import com.patrick.zombiesarereal.ai.EntityAIInvestigateSound;
import com.patrick.zombiesarereal.ai.EntityAINonSneakingNearestAttackableTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CustomBaseZombie extends EntityZombie
{
    public static final int    FOLLOW_RANGE                   = 20;
    public static final double MIN_SPEED                      = 0.20D;
    public static final double MAX_SPEED                      = 0.40D;
    public static final double MAX_HEALTH                     = 10.0F;
    public static final double MIN_HEALTH                     = 1.0F;
    public static final double MIN_ATTACK_DAMAGE              = 1.0F;
    public static final double MAX_ATTACK_DAMAGE              = 8.0F;
    public static final double PASSIVE_SPEED_MULTIPLIER       = 0.4D;
    public static final double INVESTIGATION_SPEED_MULTIPLIER = 0.8D;

    public CustomBaseZombie(World worldIn)
    {
        super(worldIn);
        this.enablePersistence();
        this.setHealth(getRandomHealth());
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
        this.targetTasks.addTask(2, new EntityAINonSneakingNearestAttackableTarget(this, EntityPlayer.class, true));
        this.tasks.addTask(3, new EntityAIInvestigateSound(this, INVESTIGATION_SPEED_MULTIPLIER));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, PASSIVE_SPEED_MULTIPLIER));
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
