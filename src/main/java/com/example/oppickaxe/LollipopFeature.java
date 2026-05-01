package com.example.oppickaxe;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class LollipopFeature extends Feature<DefaultFeatureConfig> {
    public LollipopFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos origin = context.getOrigin();

        // Place 4 oak fence blocks as the stick
        for (int i = 0; i < 4; i++) {
            context.getWorld().setBlockState(origin.up(i), Blocks.OAK_FENCE.getDefaultState(), 3);
        }

        // Place 3x3x3 ball of wool at the top
        BlockPos top = origin.up(4);
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    int r = context.getRandom().nextInt(3);
                    net.minecraft.block.Block wool = r == 0 ? Blocks.MAGENTA_WOOL : r == 1 ? Blocks.PINK_WOOL : Blocks.LIGHT_BLUE_WOOL;
                    context.getWorld().setBlockState(top.add(x, y, z), wool.getDefaultState(), 3);
                }
            }
        }

        return true;
    }
}
