package com.patrick.zombiesarereal.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KnockbackHelper
{
    public static void applyKnockbackToLivingEntity(EntityPlayer player, Entity target, boolean swingArm)
    {
        if (target instanceof EntityLivingBase && !isNonLivingEntity(target))
        {
            applyKnockbackToLivingEntity(player, target);
            if (swingArm) swingArm(player);
        }
    }

    private static void applyKnockbackToLivingEntity(EntityPlayer player, Entity target)
    {
        double knockbackStrength = 0.8;
        Vec3d  lookVec           = player.getLookVec();
        target.motionX += lookVec.x * knockbackStrength;
        target.motionZ += lookVec.z * knockbackStrength;
    }

    private static boolean isNonLivingEntity(Entity entity)
    {
        //entity instanceof EntityEgg ||
        //entity instanceof EntityPotion ||
        //entity instanceof EntityExpBottle ||
        //entity instanceof EntityFireworkRocket ||
        //entity instanceof EntityArrow ||
        //entity instanceof EntitySnowball ||
        //entity instanceof EntityTNTPrimed ||
        return entity instanceof EntityBoat ||
                entity instanceof EntityMinecart ||
                entity instanceof EntityItem ||
                entity instanceof EntityEnderPearl ||
                entity instanceof EntityItemFrame ||
                entity instanceof EntityPainting ||
                entity instanceof EntityXPOrb;
    }

    @SideOnly(Side.CLIENT)
    private static void swingArm(EntityPlayer player)
    {
        player.swingArm(EnumHand.MAIN_HAND);
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
    }
}
