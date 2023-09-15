package com.patrick.zombiesarereal.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAILookDown extends EntityAIBase
{

    private final EntityLiving entity;

    public EntityAILookDown(EntityLiving entityIn)
    {
        this.entity = entityIn;
        this.setMutexBits(2);  // This AI action requires looking
    }

    @Override
    public boolean shouldExecute()
    {
        // Only execute if the entity is not currently attacking
        return entity.getAttackTarget() == null;
    }

    @Override
    public void updateTask()
    {
        this.entity
                .getLookHelper()
                .setLookPosition(
                        this.entity.posX, this.entity.posY, this.entity.posZ,
                        (float) this.entity.getHorizontalFaceSpeed(),
                        (float) this.entity.getVerticalFaceSpeed()
                );
    }
}
