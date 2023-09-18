package com.patrick.zombiesarereal.helpers;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class SpeedHelper
{
    private static final UUID SPEED_MODIFIER_ID = UUID.fromString("8aabbf58-3b6d-4f8e-9abe-9cfc3f6fcbd5");
    private static final int  JUMP_COOLDOWN     = 10; // 90 seconds in ticks
    private static final int  MAX_ENERGY        = 1800; // ticks
    private static       int  energy            = MAX_ENERGY;
    private static       int  jumpCooldown      = 0;

    public static void onPlayerJumped(EntityPlayer player)
    {
        jumpCooldown = JUMP_COOLDOWN;
        energy       = Math.max(0, energy - 20);
        applySpeedModifier(player, 0.075);
    }

    public static void updatePlayerSpeed(EntityPlayer player)
    {
        if (player.isSprinting()) energy = Math.max(0, energy - 1);
        else energy = Math.min(MAX_ENERGY, energy + 2);

        if (jumpCooldown > 0)
        {
            jumpCooldown--;
            return;
        }

        double speedModifierValue = getSpeedModifierValue();
        applySpeedModifier(player, speedModifierValue);
    }

    private static double getSpeedModifierValue() {
        double modifier = 0.5 + 0.5 * (energy / (double) MAX_ENERGY);

        if (modifier > 0.85) {
            return 1.0;
        } else if (modifier > 0.5) {
            // Map the range 0.5-0.85 to 0.5-1.0 linearly
            return 0.5 + 2.0 * (modifier - 0.5);
        }

        return modifier;
    }


    private static void applySpeedModifier(EntityPlayer player, double speedModifierValue)
    {
        IAttributeInstance movementAttribute = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        AttributeModifier currentModifier = movementAttribute.getModifier(SPEED_MODIFIER_ID);
        if (currentModifier != null) movementAttribute.removeModifier(currentModifier);

        AttributeModifier newModifier = new AttributeModifier(SPEED_MODIFIER_ID, "Energy speed modifier",
                speedModifierValue - 1, 2
        );
        movementAttribute.applyModifier(newModifier);
    }
}
