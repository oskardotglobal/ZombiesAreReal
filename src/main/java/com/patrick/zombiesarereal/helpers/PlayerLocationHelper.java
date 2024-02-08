package com.patrick.zombiesarereal.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerLocationHelper {
    private static final Map<UUID, BlockPos> lastPlayerPositions = new HashMap<>();

    public static boolean hasChangePosition(EntityPlayer player) {
        BlockPos currentPos = player.getPosition();
        BlockPos lastPos = lastPlayerPositions.computeIfAbsent(player.getUniqueID(), k -> currentPos);

        return !lastPos.equals(currentPos);
    }

    public static void setPlayerPosition(EntityPlayer player) {
        lastPlayerPositions.put(player.getUniqueID(), player.getPosition());
    }

    public static void clearPlayerData(EntityPlayer player) {
        lastPlayerPositions.remove(player.getUniqueID());
    }
}
