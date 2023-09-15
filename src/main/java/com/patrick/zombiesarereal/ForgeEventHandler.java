package com.patrick.zombiesarereal;

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

}
