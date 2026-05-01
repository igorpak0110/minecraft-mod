package com.example.oppickaxe;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CandyCaneFeature extends Feature<DefaultFeatureConfig> {
    public CandyCaneFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos origin = context.getOrigin();
        int height = 6 + context.getRandom().nextInt(3); // 6-8 blocks

        for (int i = 0; i < height; i++) {
            net.minecraft.block.Block block = (i % 2 == 0) ? Blocks.RED_CONCRETE : Blocks.WHITE_CONCRETE;
            context.getWorld().setBlockState(origin.up(i), block.getDefaultState(), 3);
        }

        return true;
    }
}
