package com.patrick.zombiesarereal.helpers;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ArmsEnergyHelper
{
    private static final int MAX_ENERGY       = 1500; // in ticks
    private static final int MIN_HIT_COOLDOWN = 20; // in seconds
    private static final int MAX_HIT_COOLDOWN = 100; // in seconds
    public static final  int HIT_ENERGY_COST  = 80;

    private static final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    static class PlayerData
    {

        int energy      = MAX_ENERGY;
        int hitCooldown = 0;
    }

    public static boolean canHit(EntityPlayer player)
    {
        PlayerData data   = playerDataMap.computeIfAbsent(player.getUniqueID(), k -> new PlayerData());
        boolean    canHit = data.hitCooldown == 0;
        if (!canHit)
        {
            swingCamera(player);
            player.performHurtAnimation();
        }
        return canHit;
    }

    public static void onPlayerHit(EntityPlayer player)
    {
        // Ensure it runs only once per tick (to avoid dual-side execution on servers)
        if (player.world.isRemote) return;

        if (playerDataMap.size() > 1000) playerDataMap.clear();

        PlayerData data = playerDataMap.computeIfAbsent(player.getUniqueID(), k -> new PlayerData());

        data.hitCooldown = calcHitCooldown(data.energy);
        data.energy      = Math.max(0, data.energy - HIT_ENERGY_COST);
    }

    public static void onPlayerTick(EntityPlayer player)
    {
        // Ensure it runs only once per tick (to avoid dual-side execution on servers)
        if (player.world.isRemote) return;

        PlayerData data = playerDataMap.computeIfAbsent(player.getUniqueID(), k -> new PlayerData());

        data.energy = Math.min(MAX_ENERGY, data.energy + 1);

        if (data.hitCooldown > 0)
        {
            data.hitCooldown--;
        }
    }

    private static void swingCamera(EntityPlayer player)
    {
        Random rand       = new Random();
        float  deltaYaw   = rand.nextBoolean() ? 30 : -30;
        float  deltaPitch = rand.nextBoolean() ? 15 : -15;
        player.rotationYaw -= deltaYaw;
        player.rotationPitch += deltaPitch;
    }

    private static int calcHitCooldown(int energy)
    {
        return (int) (MAX_HIT_COOLDOWN - (energy / (float) MAX_ENERGY) * (MAX_HIT_COOLDOWN - MIN_HIT_COOLDOWN));
    }
}
