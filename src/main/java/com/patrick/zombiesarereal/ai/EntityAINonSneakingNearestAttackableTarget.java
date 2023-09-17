package com.patrick.zombiesarereal.ai;

import com.patrick.zombiesarereal.entities.CustomBaseZombie;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAINonSneakingNearestAttackableTarget extends EntityAINearestAttackableTarget<EntityPlayer>
{
    public EntityAINonSneakingNearestAttackableTarget(EntityCreature creature, Class<EntityPlayer> classTarget, boolean checkSight)
    {
        super(creature, classTarget, checkSight);
    }

    @Override
    public boolean shouldExecute()
    {
        if (super.shouldExecute())
        {
            EntityPlayer nearestPlayer = this.taskOwner.world.getNearestPlayerNotCreative(this.taskOwner, CustomBaseZombie.FOLLOW_RANGE);
            if (nearestPlayer == null)
            {
                return false;
            }
            if (nearestPlayer.isSneaking() && this.taskOwner.getDistance(nearestPlayer) > 3.0D)
            {
                return false;
            }
            return true;
        }
        return false;
    }
}
