package com.patrick.zombiesarereal;

import com.patrick.zombiesarereal.entities.HordeZombie;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ZombieHordeGenerator
{
    private static final int    HORDE_INITIAL_DISTANCE = 97;
    private static final Random rand                   = new Random();
    public static final  int    HORDE_SIZE             = 50;

    public static void generate(World world)
    {
        for (EntityPlayer player : world.playerEntities)
        {
            BlockPos playerPos = player.getPosition();
            int         pointX           = playerPos.getX() - HORDE_INITIAL_DISTANCE;
            for (int i = 0; i < HORDE_SIZE; i++)
            {
                int         randomPointX = pointX - (rand.nextInt(8) - 4);
                int         randomPointZ = playerPos.getZ() - (rand.nextInt(48) - 24);
                BlockPos    zombiePos    = world.getTopSolidOrLiquidBlock(new BlockPos(randomPointX, 0, randomPointZ));
                IBlockState blockState   = world.getBlockState(zombiePos.down());

                // TODO: Remove comment
                // if (zombiePos.getY() >= 26) break;
                if (!world.canSeeSky(zombiePos)) break;
                if (!isAllowedBlock(blockState)) break;

                HordeZombie hordeZombie = new HordeZombie(world);
                hordeZombie.setLocationAndAngles(
                        zombiePos.getX() + 0.5, zombiePos.getY(), zombiePos.getZ() + 0.5,
                        rand.nextFloat() * 360.0F, 0.0F
                );
                world.spawnEntity(hordeZombie);
            }
        }
    }

    private static boolean isAllowedBlock(IBlockState state)
    {
        Block block = state.getBlock();
        return block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.SAND || block == Blocks.GRAVEL;
    }
}
