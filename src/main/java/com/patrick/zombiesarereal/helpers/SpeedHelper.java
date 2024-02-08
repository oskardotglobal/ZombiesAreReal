package com.patrick.zombiesarereal.helpers;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpeedHelper {
    private static final UUID SPEED_MODIFIER_ID = UUID.fromString("8aabbf58-3b6d-4f8e-9abe-9cfc3f6fcbd5");
    private static final int JUMP_COOLDOWN = 10; // ticks
    private static final int MAX_ENERGY = 1800; // 90 seconds in ticks

    // TODO: clean it regularly
    private static final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    static class PlayerData {
        int energy = MAX_ENERGY;
        int jumpCooldown = 0;
    }

    public static void onPlayerJumped(EntityPlayer player) {
        // Ensure it runs only once per tick (to avoid dual-side execution on servers)
        if (player.world.isRemote) return;

        PlayerData data = playerDataMap.computeIfAbsent(player.getUniqueID(), k -> new PlayerData());

        data.jumpCooldown = JUMP_COOLDOWN;
        data.energy = Math.max(0, data.energy - 40);
        applySpeedModifier(player, 0.075);
    }

    public static void updatePlayerSpeed(EntityPlayer player) {
        // Ensure it runs only once per tick (to avoid dual-side execution on servers)
        if (player.world.isRemote) return;

        PlayerData data = playerDataMap.computeIfAbsent(player.getUniqueID(), k -> new PlayerData());

        if (player.isSprinting()) data.energy = Math.max(0, data.energy - 1);
        else data.energy = Math.min(MAX_ENERGY, data.energy + 2);

        if (data.jumpCooldown > 0) {
            data.jumpCooldown--;
            return;
        }
        double speedModifier = getSpeedModifierValue(data);
        applySpeedModifier(player, speedModifier);
    }

    private static void applySpeedModifier(EntityPlayer player, double speedModifier) {
        IAttributeInstance movementAttribute = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        AttributeModifier currentModifier = movementAttribute.getModifier(SPEED_MODIFIER_ID);
        if (currentModifier != null) movementAttribute.removeModifier(currentModifier);

        AttributeModifier newModifier = new AttributeModifier(SPEED_MODIFIER_ID, "Energy speed modifier",
                speedModifier - 1, 2
        );
        movementAttribute.applyModifier(newModifier);
    }

    private static double getSpeedModifierValue(PlayerData data) {
        return 0.5 + 0.5 * (data.energy / (double) MAX_ENERGY);
    }
}
