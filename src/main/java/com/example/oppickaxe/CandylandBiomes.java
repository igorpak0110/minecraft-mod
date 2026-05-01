package com.example.oppickaxe;

import net.fabricmc.fabric.api.biome.v1.OverworldBiomes;
import net.fabricmc.fabric.api.biome.v1.OverworldClimate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class CandylandBiomes {
    public static final RegistryKey<Biome> CANDYLAND = RegistryKey.of(
            RegistryKeys.BIOME,
            Identifier.of("oppickaxe", "candyland")
    );

    public static void register() {
        OverworldBiomes.addContinentalBiome(CANDYLAND, OverworldClimate.TEMPERATE, 2.0);
    }
}
