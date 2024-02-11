package com.patrick.zombiesarereal;

import com.patrick.zombiesarereal.entities.HordeZombie;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Random;

public class ZombieHordeGenerator {
    private static final int HORDE_INITIAL_DISTANCE = 48;
    private static final int HORDE_SIZE = 20;

    private static final Random rand = new Random();

    private static BlockPos getHordePos(HordeZombie.Direction direction) {
        switch (direction) {
            case WEST:
                return new BlockPos(-HORDE_INITIAL_DISTANCE, 0, 0);
            case NORTHEAST:
                return new BlockPos(HORDE_INITIAL_DISTANCE / 2, 0, -HORDE_INITIAL_DISTANCE);
            case SOUTHEAST:
                return new BlockPos(HORDE_INITIAL_DISTANCE / 2, 0, HORDE_INITIAL_DISTANCE);

            default:
                return BlockPos.ORIGIN;
        }
    }

    private static BlockPos getZombiePos(HordeZombie.Direction direction) {
        BlockPos hordePos = getHordePos(direction);

        if (direction == HordeZombie.Direction.WEST) {
            return hordePos.add(
                    new Vec3i((rand.nextInt(8) - 4), 0, (rand.nextInt(48) - 24))
            );
        }

        return hordePos.add(
                new Vec3i((rand.nextInt(48) - 24), 0, (rand.nextInt(8) - 4))
        );
    }

    public static void generate(World world) {
        // O(n^3), yikes
        for (EntityPlayer player : world.playerEntities) {
            BlockPos playerPos = player.getPosition();

            for (HordeZombie.Direction direction : HordeZombie.Direction.values()) {
                for (int i = 0; i < HORDE_SIZE; i++) {
                    BlockPos zombiePos = playerPos.add(getZombiePos(direction));
                    IBlockState blockState = world.getBlockState(zombiePos.down());

                    if (!world.canSeeSky(zombiePos)) break;
                    if (!ZombieWorldGen.isAllowedBlock(blockState)) break;

                    HordeZombie hordeZombie = new HordeZombie(world);
                    hordeZombie.setHordeTarget(direction, playerPos);

                    hordeZombie.setLocationAndAngles(
                            zombiePos.getX() + 0.5, world.getTopSolidOrLiquidBlock(zombiePos).getY(), zombiePos.getZ() + 0.5,
                            rand.nextFloat() * 360.0F, 0.0F
                    );

                    world.spawnEntity(hordeZombie);
                }
            }
        }
    }
}
