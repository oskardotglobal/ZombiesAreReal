package com.patrick.zombiesarereal;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;

public class ModEntityHandler
{
    public static void removeZombiesEntitiesFromEntry()
    {
        for (Biome biome : ForgeRegistries.BIOMES)
        {
            List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.MONSTER);
            spawnList.removeIf(entry -> entry.entityClass == EntityZombie.class);
        }
    }

}