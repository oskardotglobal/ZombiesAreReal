package com.patrick.zombiesarereal;

import com.patrick.zombiesarereal.utils.KnockbackUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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
            Entity        target = event.getTarget();
            KnockbackUtil.applyKnockbackToLivingEntity(player, target, false);
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
        KnockbackUtil.applyKnockbackToLivingEntity(player, target, true);
    }
}
