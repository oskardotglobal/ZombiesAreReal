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
}
