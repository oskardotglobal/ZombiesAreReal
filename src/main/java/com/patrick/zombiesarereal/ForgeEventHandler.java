package com.patrick.zombiesarereal;

import com.patrick.zombiesarereal.helpers.KnockbackHelper;
import com.patrick.zombiesarereal.helpers.SpeedHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EventBusSubscriber
public class ForgeEventHandler
{

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (!event.world.isRemote && event.phase == TickEvent.Phase.START)
        {
            long timeOfDay = event.world.getWorldTime() % 24000;
            if (timeOfDay == 13000 || timeOfDay == 23000)
            {
                ZombieHordeGenerator.generate(event.world);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerAttackEntity(AttackEntityEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();

        if (player.getHeldItemMainhand().isEmpty() && event.isCancelable())
        {
            event.setCanceled(true);
            Entity target = event.getTarget();
            KnockbackHelper.applyKnockbackToLivingEntity(player, target, false);
        }
    }

    @SubscribeEvent
    public static void onEntityKnockback(LivingKnockBackEvent event)
    {
        if (event.isCancelable()) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerRightClickEntity(PlayerInteractEvent.EntityInteract event)
    {
        EntityPlayer player = event.getEntityPlayer();
        Entity       target = event.getTarget();
        KnockbackHelper.applyKnockbackToLivingEntity(player, target, true);
    }

    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event)
    {
        if (event.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            SpeedHelper.onPlayerJumped(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if ((event.getEntity() instanceof EntityPlayer))
        {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            SpeedHelper.updatePlayerSpeed(player);
        }
    }

    @SubscribeEvent
    public static void onFOVUpdate(FOVUpdateEvent event)
    {
        // Prevent the fov reduction to bee too annoying when jumping
        event.setNewfov(Math.max(0.95F, event.getFov()));
    }
}
