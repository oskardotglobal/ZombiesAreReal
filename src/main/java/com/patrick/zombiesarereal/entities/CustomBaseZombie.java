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
    public static final int    FOLLOW_RANGE             = 35;
    public static final double ACTIVE_SPEED             = 0.37D;
    public static final double PASSIVE_SPEED_MULTIPLIER = 0.4D;

    public CustomBaseZombie(World worldIn)
    {
        super(worldIn);

        this.enablePersistence();
        this.setHealth(4.0F);

        /*Iterator<EntityAITasks.EntityAITaskEntry> it = this.tasks.taskEntries.iterator();
        while (it.hasNext())
        {
            EntityAIBase ai = it.next().action;
            if (ai instanceof EntityAIWander)
            {
                it.remove();
            }
        }*/
    }

    @Override
    protected void initEntityAI()
    {
        this.tasks.addTask(2, new EntityAIZombieAttack(this, 1.0D, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.tasks.addTask(4, new EntityAILookDown(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, PASSIVE_SPEED_MULTIPLIER));
        this.tasks.addTask(6, new EntityAIEasternWander(this, PASSIVE_SPEED_MULTIPLIER, 1));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, PASSIVE_SPEED_MULTIPLIER));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, FOLLOW_RANGE));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(FOLLOW_RANGE);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(ACTIVE_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }

    @Override
    protected boolean shouldBurnInDay()
    {
        return false;
    }
}
