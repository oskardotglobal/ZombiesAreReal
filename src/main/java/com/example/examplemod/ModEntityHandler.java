package com.example.examplemod;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Iterator;
import java.util.List;

public class ModEntityHandler
{

    public static void removeEntitiesExceptZombies()
    {
        for (Biome biome : ForgeRegistries.BIOMES)
        {
            List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.MONSTER);

            // Use iterator to safely remove elements during iteration
            Iterator<Biome.SpawnListEntry> iterator = spawnList.iterator();
            while (iterator.hasNext())
            {
                Biome.SpawnListEntry entry = iterator.next();
                if (entry.entityClass != EntityZombie.class)
                {
                    iterator.remove();
                }
            }

            // Similarly, for other entity types (Creature, Ambient, WaterCreature)
            iterator = biome.getSpawnableList(EnumCreatureType.CREATURE).iterator();
            while (iterator.hasNext())
            {
                iterator.next();
                iterator.remove();
            }

            iterator = biome.getSpawnableList(EnumCreatureType.AMBIENT).iterator();
            while (iterator.hasNext())
            {
                iterator.next();
                iterator.remove();
            }

            iterator = biome.getSpawnableList(EnumCreatureType.WATER_CREATURE).iterator();
            while (iterator.hasNext())
            {
                iterator.next();
                iterator.remove();
            }
        }
    }
}