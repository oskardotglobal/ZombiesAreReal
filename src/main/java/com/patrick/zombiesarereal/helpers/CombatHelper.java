package com.patrick.zombiesarereal.helpers;

import com.patrick.zombiesarereal.entities.CustomBaseZombie;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.Random;

public class CombatHelper {
    public static final Random rand = new Random();

    public static boolean isCoherentWeapon(Item heldItem) {
        boolean isCoherentObject = false;
        if (heldItem instanceof ItemBlock) {
            Block block = ((ItemBlock) heldItem).getBlock();
            boolean isNotCoherentObject = block == Blocks.DIRT ||
                    block == Blocks.GRASS ||
                    block == Blocks.SAND ||
                    block == Blocks.GRAVEL ||
                    block == Blocks.WOOL ||
                    block == Blocks.FLOWER_POT ||
                    block == Blocks.RED_FLOWER ||  // Excluding all kinds of flowers
                    block == Blocks.YELLOW_FLOWER ||
                    block == Blocks.BROWN_MUSHROOM ||  // Excluding mushrooms
                    block == Blocks.RED_MUSHROOM ||
                    block == Blocks.MELON_BLOCK ||
                    block == Blocks.PUMPKIN ||
                    block == Blocks.LIT_PUMPKIN ||
                    block == Blocks.WHEAT ||
                    block == Blocks.CARROTS ||
                    block == Blocks.POTATOES ||
                    block == Blocks.BEETROOTS;
            if (!isNotCoherentObject)
                isCoherentObject = block.getDefaultState().isOpaqueCube();
        } else {
            isCoherentObject = heldItem instanceof ItemSword ||
                    heldItem instanceof ItemAxe ||
                    heldItem instanceof ItemPickaxe ||
                    heldItem instanceof ItemSpade ||
                    heldItem instanceof ItemHoe ||
                    heldItem == Items.STICK ||
                    heldItem == Items.BUCKET ||
                    heldItem == Items.SHEARS ||
                    heldItem == Items.BONE;
        }
        return isCoherentObject;
    }

    public static void tryToLocalizeDamage(CustomBaseZombie zombie, EntityPlayer player, LivingHurtEvent event) {
        CombatHelper.tryToLocalizeMeleeDamage(zombie, player, event);
        // TODO: Remake localized damage system for projectiles
        // fuck this, I don't care if it doesn't work
        boolean canLocalize = CombatHelper.tryToLocalizeMeleeDamage(zombie, player, event);
        if (!canLocalize) tryToLocalizeProjectileDamage(zombie, event);
    }

    private static void tryToLocalizeProjectileDamage(CustomBaseZombie zombie, LivingHurtEvent event) {
        DamageSource source = event.getSource();

        if (source.getImmediateSource() != null) {
            Vec3d hitVec = source.getImmediateSource().getPositionVector();
            double hitY = hitVec.y;
            float amount = event.getAmount();

            if (event.isCancelable()) event.setCanceled(true);

            double headTop = zombie.posY + 2.0D;
            double headBottom = zombie.posY + 1.5D;
            double torsoTop = headBottom;
            double torsoBottom = zombie.posY + 1.0D;
            double legsTop = torsoBottom;
            double legsBottom = zombie.posY;

            if (hitY > headBottom && hitY <= headTop) // Head
            {
                zombie.reduceHeadHealth(amount);
            } else if (hitY > torsoBottom && hitY <= torsoTop) // Torso
            {
                boolean isLeftArm = rand.nextBoolean();
                if (isLeftArm) zombie.reduceLeftArmHealth(amount);
                else zombie.reduceRightArmHealth(amount);
            } else if (hitY > legsBottom && hitY <= legsTop) // Legs
            {
                boolean isLeftLeg = rand.nextBoolean();
                if (isLeftLeg) zombie.reduceLeftLegHealth(amount);
                else zombie.reduceRightLegHealth(amount);
            }
        }
    }

    private static boolean tryToLocalizeMeleeDamage(CustomBaseZombie zombie, EntityPlayer player, LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (!(source instanceof EntityDamageSource)) return false;
        if (source instanceof EntityDamageSourceIndirect) return false;
        if (source.isProjectile()) return false;
        if (source.isUnblockable()) return false;

        if (event.isCancelable()) event.setCanceled(true);

        float pitch = player.rotationPitch;
        float amount = event.getAmount();

        // if player is jumping/in the air, force headshot
        if (!player.onGround) {
            zombie.reduceHeadHealth(amount);
            return true;
        }

        if (pitch > 25) // Leg
        {
            boolean isLeftLeg = rand.nextBoolean();
            if (isLeftLeg) zombie.reduceLeftLegHealth(amount);
            else zombie.reduceRightLegHealth(amount);
        } else if (pitch > 5) // Torso
        {
            boolean isLeftArm = rand.nextBoolean();
            if (isLeftArm) zombie.reduceLeftArmHealth(amount);
            else zombie.reduceRightArmHealth(amount);
        } else // Head
        {
            zombie.reduceHeadHealth(amount);
        }
        return true;
    }
}
