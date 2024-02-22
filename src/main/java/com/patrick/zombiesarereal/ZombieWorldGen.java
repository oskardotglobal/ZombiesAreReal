package com.patrick.zombiesarereal;

import com.patrick.zombiesarereal.entities.TerrainZombie;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Objects;
import java.util.Random;

public class ZombieWorldGen implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
                         IChunkProvider chunkProvider) {
        if (world.provider.getDimension() == 0) {
            this.runGenerator(chunkX, chunkZ, world, random);
        }
    }

    private void runGenerator(int chunkX, int chunkZ, World world, Random rand) {
        if (rand.nextInt(5) == 0) {
            int x = chunkX * 16 + rand.nextInt(16);
            int z = chunkZ * 16 + rand.nextInt(16);
            BlockPos topSolidOrLiquidBlock = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z));
            int y = topSolidOrLiquidBlock.getY();
            IBlockState blockState = world.getBlockState(topSolidOrLiquidBlock);
            IBlockState blockStateUnder = world.getBlockState(topSolidOrLiquidBlock.down());
            if (y > 0 && isAllowedBlock(blockStateUnder) && isAllowedBlock(blockState)) {
                EntityZombie zombie = new TerrainZombie(world);
                zombie.setLocationAndAngles(x + 0.5, y, z + 0.5, rand.nextFloat() * 360.0F, 0.0F);

                for (int i = 0; i < 3; i++) {
                    world.spawnEntity(zombie);
                }
            }
        }
    }

    public static boolean isAllowedBlock(IBlockState state) {
        Block block = state.getBlock();

        // List of blocks we consider as part of "trees".
        // This is a simplified check, but should work for vanilla trees.
        ResourceLocation[] treeBlocks = {
                Block.REGISTRY.getNameForObject(Blocks.LOG),
                Block.REGISTRY.getNameForObject(Blocks.LOG2),
                Block.REGISTRY.getNameForObject(Blocks.LEAVES),
                Block.REGISTRY.getNameForObject(Blocks.LEAVES2)
        };

        // Check against water
        if (block == Blocks.WATER) {
            return false;
        }

        // Check against tree blocks
        for (ResourceLocation treeBlock : treeBlocks) {
            if (Objects.equals(block.getRegistryName(), treeBlock)) {
                return false;
            }
        }

        return true; // If passed both checks, it's an allowed block
    }
}
