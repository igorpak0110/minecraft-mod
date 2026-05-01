package com.example.oppickaxe;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

public class CandylandBiomes implements TerraBlenderApi {
    public static final RegistryKey<Biome> CANDYLAND = RegistryKey.of(
            RegistryKeys.BIOME,
            Identifier.of("oppickaxe", "candyland")
    );

    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new CandylandRegion(Identifier.of("oppickaxe", "overworld"), 4));
    }
}
