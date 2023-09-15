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
        if (event.phase == TickEvent.Phase.START && !event.world.isRemote &&
                (event.world.getWorldTime() % 24000 == 13000 || event.world.getWorldTime() % 24000 == 23000)
        )
        {
            ZombieHordeGenerator.generate(event.world);
        }
    }

}
