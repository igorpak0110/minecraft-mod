package com.example.oppickaxe;

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
        // Biome is registered via data/oppickaxe/worldgen/biome/candyland.json
        // Apply in-game with: /fillbiome ~ ~ ~ ~50 ~10 ~50 oppickaxe:candyland
    }
}
