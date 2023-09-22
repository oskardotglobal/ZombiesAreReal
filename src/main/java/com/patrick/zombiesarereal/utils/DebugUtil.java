package com.patrick.zombiesarereal.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class DebugUtil
{
    public static void spawnNoteParticleAtBlockPos(World world, BlockPos pos)
    {
        if (world.isRemote) return;
        for (int i = 0; i < 1000; i++)
        {
            EnumParticleTypes particleID = EnumParticleTypes.NOTE;
            SPacketParticles packet = new SPacketParticles(particleID,
                    false,
                    pos.getX() + 0.5f, (pos.getY() + 1) + 0.5f, pos.getZ() + 0.5f,
                    0, 0, 0,
                    1,
                    0
            );

            for (EntityPlayerMP player : world.getEntitiesWithinAABB(
                    EntityPlayerMP.class,
                    new AxisAlignedBB(pos).grow(64)
            ))
            {
                player.connection.sendPacket(packet);
            }
        }
    }

    public static void showChat(EntityPlayer player, String message)
    {
        TextComponentString text = new TextComponentString(message);
        text.getStyle().setColor(TextFormatting.DARK_GRAY);
        player.sendMessage(text);
    }

}
