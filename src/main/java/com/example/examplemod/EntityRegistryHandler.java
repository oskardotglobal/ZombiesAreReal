package com.example.examplemod;

import com.example.examplemod.entities.TerrainZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

@Mod.EventBusSubscriber
public class EntityRegistryHandler
{
    private static final int ENTITY_ID = 93244332;

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event)
    {
        EntityEntry entry = EntityEntryBuilder
                .create()
                .entity(TerrainZombie.class)
                .id(new ResourceLocation(ExampleMod.MODID, "terrain_zombie"), ENTITY_ID)
                .name("terrain_zombie")
                .tracker(80, 3, true)
                .egg(0xFFFFFF, 0xAAAAAA) // Egg colors for the spawn egg.
                .build();
        event.getRegistry().registerAll(entry);
    }

}
