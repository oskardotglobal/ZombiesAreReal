package com.example.examplemod.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAILookIdle;

import java.util.Random;

public class EntityAIErraticLookIdle extends EntityAILookIdle
{

    private final EntityLiving idleEntity;
    private       double       lookX;
    private       double       lookZ;
    private       int          idleTime;

    private final Random rand = new Random();

    public EntityAIErraticLookIdle(EntityLiving entitylivingIn)
    {
        super(entitylivingIn);
        this.idleEntity = entitylivingIn;
    }

    /*@Override
    public boolean shouldContinueExecuting()
    {
        return this.idleTime > 0;
    }*/

    @Override
    public boolean shouldExecute()
    {
        if (this.idleEntity.getRNG().nextFloat() < 0.02F)
        {
            this.lookX = (float) Math.random() * 2.0F - 1.0F;
            this.lookZ = (float) Math.random() * 2.0F - 1.0F;
            this.idleTime = 20 + this.idleEntity.getRNG().nextInt(20);
            return true;
        }
        return false;
    }

    @Override
    public void updateTask()
    {
        --this.idleTime;

        // Introducing spasms
        double jitterAmountHorizontal = 0.8D;  // Increased from 0.5D for wider movements
        double jitterAmountVertical   = 0.2D;    // Reduced for a slight downward movement

        double randomX = this.idleEntity.posX + this.lookX + (rand.nextDouble() - 0.5D) * jitterAmountHorizontal;

        // Decreased vertical jitter for slight downward look
        double randomY = this.idleEntity.posY + this.idleEntity.getEyeHeight() - jitterAmountVertical;

        double randomZ = this.idleEntity.posZ + this.lookZ + (rand.nextDouble() - 0.5D) * jitterAmountHorizontal;

        this.idleEntity
                .getLookHelper()
                .setLookPosition(
                        randomX, randomY, randomZ,
                        (float) this.idleEntity.getHorizontalFaceSpeed(),
                        (float) this.idleEntity.getVerticalFaceSpeed()
                );
    }

}
