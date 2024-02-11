package com.patrick.zombiesarereal.entities;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HordeZombie extends CustomBaseZombie {
    private BlockPos hordeTargetPos;
    private Direction hordeTargetDirection;

    public HordeZombie(World worldIn) {
        super(worldIn);
    }

    public BlockPos getHordeTargetPos() {
        return hordeTargetPos;
    }

    public Direction getHordeTargetDirection() {
        return hordeTargetDirection;
    }

    public void setHordeTarget(Direction direction, BlockPos targetPos) {
        hordeTargetPos = targetPos;
        hordeTargetDirection = direction;
    }

    public enum Direction {
        WEST,
        NORTHEAST,
        SOUTHEAST
    }
}
