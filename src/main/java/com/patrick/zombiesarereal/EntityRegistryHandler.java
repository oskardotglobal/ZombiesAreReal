package com.patrick.zombiesarereal;

import com.patrick.zombiesarereal.entities.HordeZombie;
import com.patrick.zombiesarereal.entities.TerrainZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

@Mod.EventBusSubscriber
public class EntityRegistryHandler
{
    private static final int TERRAIN_ZOMBIE_ID = 93244332;
    private static final int HORDE_ZOMBIE_ID = 93244333;

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event)
    {
        registerTerrainZombie(event);
        registerHordeZombie(event);
    }

    private static void registerHordeZombie(RegistryEvent.Register<EntityEntry> event)
    {
        EntityEntry entry = EntityEntryBuilder
                .create()
                .entity(HordeZombie.class)
                .id(new ResourceLocation(ZombiesAreReal.MODID, "horde_zombie"), HORDE_ZOMBIE_ID)
                .name("Horde Zombie")
                .tracker(80, 3, true)
                .egg(0xFFFFFF, 0xAAAAAA) // Egg colors for the spawn egg.
                .build();
        event.getRegistry().registerAll(entry);
    }

    private static void registerTerrainZombie(RegistryEvent.Register<EntityEntry> event)
    {
        EntityEntry entry = EntityEntryBuilder
                .create()
                .entity(TerrainZombie.class)
                .id(new ResourceLocation(ZombiesAreReal.MODID, "terrain_zombie"), TERRAIN_ZOMBIE_ID)
                .name("Terrain Zombie")
                .tracker(80, 3, true)
                .egg(0xFFFFFF, 0xAAAAAA) // Egg colors for the spawn egg.
                .build();
        event.getRegistry().registerAll(entry);
    }

}
