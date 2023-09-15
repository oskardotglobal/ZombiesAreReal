package com.example.examplemod.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class EntityAIEasternWander extends EntityAIWander
{
    private final Random rand;

    public EntityAIEasternWander(EntityCreature creature, double speedIn, int chance)
    {
        super(creature, speedIn, chance);
        rand = new Random();
    }

    @Override
    public boolean shouldExecute()
    {
        if (!this.mustUpdate)
        {
            if (this.entity.getRNG().nextInt(this.executionChance) != 0)
            {
                return false;
            }
        }

        Vec3d vec3d = this.getPosition();

        if (vec3d == null)
        {
            return false;
        }
        else
        {
            this.x          = vec3d.x;
            this.y          = vec3d.y;
            this.z          = vec3d.z;
            this.mustUpdate = false;
            return true;
        }
    }

    @Override
    protected Vec3d getPosition()
    {
        double randomEastMovement = 3 + rand.nextDouble() * 6;
        double randomZMovement    = (rand.nextDouble() * 6) - 3;

        Vec3d direction = new Vec3d(randomEastMovement, 0, randomZMovement);

        return new Vec3d(
                this.entity.posX + direction.x,
                this.entity.posY + direction.y,
                this.entity.posZ + direction.z
        );
    }

}