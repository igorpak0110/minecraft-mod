package com.example.oppickaxe;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.RaycastContext;
import net.minecraft.entity.player.PlayerEntity;

public class SuperOpPickaxeItem extends PickaxeItem {
    public SuperOpPickaxeItem(Settings settings) {
        super(SuperOpToolMaterial.INSTANCE, settings);
    }

    @Override
    public float getMiningSpeed(ItemStack stack, BlockState state) {
        return 100.0F;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (world.isClient || !(miner instanceof ServerPlayerEntity player)) {
            return super.postMine(stack, world, state, pos, miner);
        }

        Direction face = getFacing(world, player);
        int[][] offsets = getPlaneOffsets(face);
        for (int[] o : offsets) {
            BlockPos target = pos.add(o[0], o[1], o[2]);
            if (target.equals(pos)) continue;
            BlockState s = world.getBlockState(target);
            if (s.isAir() || s.getHardness(world, target) < 0) continue;
            world.breakBlock(target, true, player);
        }
        return super.postMine(stack, world, state, pos, miner);
    }

    private Direction getFacing(World world, PlayerEntity player) {
        float reach = 6.0F;
        var start = player.getCameraPosVec(1.0F);
        var look = player.getRotationVec(1.0F);
        var end = start.add(look.x * reach, look.y * reach, look.z * reach);
        BlockHitResult hit = world.raycast(new RaycastContext(start, end,
                RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
        return hit.getType() == HitResult.Type.BLOCK ? hit.getSide() : Direction.UP;
    }

    private int[][] getPlaneOffsets(Direction face) {
        return switch (face.getAxis()) {
            case Y -> new int[][]{
                    {-1,0,-1},{0,0,-1},{1,0,-1},
                    {-1,0, 0},{0,0, 0},{1,0, 0},
                    {-1,0, 1},{0,0, 1},{1,0, 1}};
            case X -> new int[][]{
                    {0,-1,-1},{0,0,-1},{0,1,-1},
                    {0,-1, 0},{0,0, 0},{0,1, 0},
                    {0,-1, 1},{0,0, 1},{0,1, 1}};
            case Z -> new int[][]{
                    {-1,-1,0},{0,-1,0},{1,-1,0},
                    {-1, 0,0},{0, 0,0},{1, 0,0},
                    {-1, 1,0},{0, 1,0},{1, 1,0}};
        };
    }
}
