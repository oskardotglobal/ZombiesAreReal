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
        return true;  // Always return true to constantly look down
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
