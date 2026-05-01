package com.example.oppickaxe;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class CandyFeatures {
    public static final Feature<DefaultFeatureConfig> LOLLIPOP = Registry.register(
        Registries.FEATURE, Identifier.of("oppickaxe", "lollipop"),
        new LollipopFeature(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> CANDY_CANE = Registry.register(
        Registries.FEATURE, Identifier.of("oppickaxe", "candy_cane"),
        new CandyCaneFeature(DefaultFeatureConfig.CODEC));

    public static void register() {}
}
