package com.example.examplemod;

import com.example.examplemod.entities.TerrainZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class ZombieWorldGen implements IWorldGenerator
{
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() == 0) {
            this.runGenerator(chunkX, chunkZ, world, random);
        }
    }

    private void runGenerator(int chunkX, int chunkZ, World world, Random rand) {
        if (rand.nextInt(10) == 0) { // Example condition for 1 in 10 chance per chunk.
            int      x                     = chunkX * 16 + rand.nextInt(16);
            int      z                     = chunkZ * 16 + rand.nextInt(16);
            BlockPos topSolidOrLiquidBlock = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z));
            int      y                     = topSolidOrLiquidBlock.getY();
            boolean isLiquid = world.getBlockState(topSolidOrLiquidBlock).getMaterial().isLiquid();
            // TODO: Prevent the spawn in trees
            if (y > 0 && !isLiquid) {
                EntityZombie zombie = new TerrainZombie(world);
                zombie.setLocationAndAngles(x + 0.5, y, z + 0.5, rand.nextFloat() * 360.0F, 0.0F);
                world.spawnEntity(zombie);
            }
        }
    }
}
