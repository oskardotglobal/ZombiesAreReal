package com.example.examplemod.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class EntityAISpasmsMove extends EntityAIWander
{

    public EntityAISpasmsMove(EntityCreature creature, double speedIn, int chance)
    {
        super(creature, speedIn, chance);
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
        return RandomPositionGenerator.findRandomTarget(this.entity, 1, 0);
    }
}
