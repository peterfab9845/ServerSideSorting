package com.peterfab.serversidesorting;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ServerSideSorting.MODID)
public class ServerSideSorting {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "serversidesorting";
    // Directly reference a slf4j logger
    protected static final Logger LOGGER = LogUtils.getLogger();
}
