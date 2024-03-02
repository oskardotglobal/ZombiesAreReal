package com.patrick.zombiesarereal.helpers;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;

public class CombatHelper {
    public static boolean isCoherentWeapon(Item heldItem) {
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

            return isNotCoherentObject || block.getDefaultState().isOpaqueCube();
        }

        return heldItem instanceof ItemSword ||
                heldItem instanceof ItemPickaxe ||
                heldItem instanceof ItemAxe ||
                heldItem instanceof ItemSpade ||
                heldItem instanceof ItemHoe ||
                heldItem == Items.STICK ||
                heldItem == Items.BUCKET ||
                heldItem == Items.SHEARS ||
                heldItem == Items.BONE;
    }
}
