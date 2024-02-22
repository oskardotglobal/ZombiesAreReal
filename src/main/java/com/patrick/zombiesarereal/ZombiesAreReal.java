package com.patrick.zombiesarereal;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = ZombiesAreReal.MODID, name = ZombiesAreReal.NAME, version = ZombiesAreReal.VERSION)
public class ZombiesAreReal {
    public static final String MODID = "zombiesarereal";
    public static final String NAME = "Zombies Are Real";
    public static final String VERSION = "2.3.1";

    public static final boolean DEBBUGING = false;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ModEntityHandler.removeZombiesEntitiesFromEntry();
    }
}
