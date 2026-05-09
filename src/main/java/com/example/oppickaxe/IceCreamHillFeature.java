package com.example.oppickaxe;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class IceCreamHillFeature extends Feature<DefaultFeatureConfig> {
    public IceCreamHillFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        int radius = 5;

        for (int y = 0; y <= radius; y++) {
            int layerRadius = radius - y;
            for (int x = -layerRadius; x <= layerRadius; x++) {
                for (int z = -layerRadius; z <= layerRadius; z++) {
                    if (x * x + z * z <= layerRadius * layerRadius) {
                        BlockPos p = origin.add(x, y, z);
                        boolean isTop = y >= radius - 1;
                        setBlockState(world, p, isTop
                                ? Blocks.PINK_CONCRETE.getDefaultState()
                                : Blocks.WHITE_CONCRETE.getDefaultState());
                    }
                }
            }
        }
        // Cherry on top
        setBlockState(world, origin.add(0, radius + 1, 0), Blocks.RED_CONCRETE.getDefaultState());
        return true;
    }
}
