package com.example.oppickaxe;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CandyCastleFeature extends Feature<DefaultFeatureConfig> {
    public CandyCastleFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();

        // Build towers at 4 corners (offset -6 to +6)
        int[] corners = {-6, 6};
        for (int cx : corners) {
            for (int cz : corners) {
                buildTower(world, origin.add(cx, 0, cz));
            }
        }

        // Build walls between corners
        buildWall(world, origin, -6, 6, -6, -6, true);   // north wall
        buildWall(world, origin, -6, 6, 6, 6, true);      // south wall
        buildWall(world, origin, -6, -6, -6, 6, false);   // west wall
        buildWall(world, origin, 6, 6, -6, 6, false);     // east wall (gate)

        return true;
    }

    private void buildTower(StructureWorldAccess world, BlockPos base) {
        for (int y = 0; y < 10; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (Math.abs(x) == 1 || Math.abs(z) == 1) {
                        BlockPos p = base.add(x, y, z);
                        setBlockState(world, p, (y % 3 == 0)
                                ? Blocks.PINK_CONCRETE.getDefaultState()
                                : Blocks.WHITE_CONCRETE.getDefaultState());
                    }
                }
            }
        }
        // Battlement top
        for (int x = -1; x <= 1; x += 2) {
            setBlockState(world, base.add(x, 10, -1), Blocks.MAGENTA_CONCRETE.getDefaultState());
            setBlockState(world, base.add(x, 10, 1), Blocks.MAGENTA_CONCRETE.getDefaultState());
        }
        for (int z = -1; z <= 1; z += 2) {
            setBlockState(world, base.add(-1, 10, z), Blocks.MAGENTA_CONCRETE.getDefaultState());
            setBlockState(world, base.add(1, 10, z), Blocks.MAGENTA_CONCRETE.getDefaultState());
        }
    }

    private void buildWall(StructureWorldAccess world, BlockPos origin,
                           int x1, int x2, int z1, int z2, boolean alongX) {
        boolean isGateWall = (x1 == 6 && x2 == 6);
        int steps = alongX ? Math.abs(x2 - x1) : Math.abs(z2 - z1);
        for (int i = 1; i < steps; i++) {
            int wx = alongX ? Math.min(x1, x2) + i : x1;
            int wz = alongX ? z1 : Math.min(z1, z2) + i;
            // Leave gate opening in east wall center
            boolean isGate = isGateWall && i >= steps / 2 - 1 && i <= steps / 2 + 1;
            int wallHeight = isGate ? 3 : 6;
            for (int y = 0; y < wallHeight; y++) {
                setBlockState(world, origin.add(wx, y, wz),
                        (y % 4 == 0) ? Blocks.PINK_CONCRETE.getDefaultState()
                                : Blocks.WHITE_CONCRETE.getDefaultState());
            }
        }
    }
}
