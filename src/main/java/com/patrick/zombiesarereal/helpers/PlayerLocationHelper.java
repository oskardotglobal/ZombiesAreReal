package com.patrick.zombiesarereal.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerLocationHelper
{
    private static final Map<UUID, BlockPos> lastPlayerPositions = new HashMap<>();

    public static boolean hasChangePosition(EntityPlayer player)
    {
        BlockPos currentPos = player.getPosition();
        BlockPos lastPos    = lastPlayerPositions.computeIfAbsent(player.getUniqueID(), k -> currentPos);

        if (!lastPos.equals(currentPos))
        {
            lastPlayerPositions.put(player.getUniqueID(), currentPos);
            return true;
        }
        return false;
    }
}
