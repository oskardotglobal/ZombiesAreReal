package com.example.examplemod.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

/*if (zombie instanceof TerrainZombie)
            {
            }*/

/*EntityPlayer closestPlayer = zombie.world.getClosestPlayerToEntity(zombie, 256);
if (closestPlayer != null && !(zombie instanceof TerrainZombie))
{
    BlockPos playerPos = closestPlayer.getPosition();
    BlockPos zombiePos = zombie.getPosition();

    int      pointX                = (int) (playerPos.getX() + T * (zombiePos.getX() - playerPos.getX()));
    int      pointZ                = (int) (playerPos.getZ() + T * (zombiePos.getZ() - playerPos.getZ()));
    BlockPos surfacePoint25Percent = world.getTopSolidOrLiquidBlock(new BlockPos(pointX, 0, pointZ));
    zombie.tasks.addTask(3, new MoveToBlockPosIA(zombie, 0.7D, surfacePoint25Percent));
}*/
public class MoveToBlockPosIA extends EntityAIWander
{
    private final BlockPos targetPos;
    private final Random rand;

    public MoveToBlockPosIA(EntityCreature creatureIn, double speedIn, BlockPos targetPos)
    {
        super(creatureIn, speedIn);
        this.targetPos = targetPos;
        rand = new Random();
    }

    @Override
    protected Vec3d getPosition()
    {
        int xOffset = rand.nextInt(17) - 8;
        int zOffset = rand.nextInt(17) - 8;

        return new Vec3d(
                this.targetPos.getX() + xOffset,
                this.targetPos.getY(),
                this.targetPos.getZ() + zOffset
        );
    }
}