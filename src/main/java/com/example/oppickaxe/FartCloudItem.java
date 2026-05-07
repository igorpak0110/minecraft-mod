package com.example.oppickaxe;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FartCloudItem extends Item {
    public FartCloudItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(world, user.getX(), user.getY(), user.getZ());
            cloud.setRadius(3.0f);
            cloud.setDuration(200);
            cloud.addEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0));
            cloud.addEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 1));
            world.spawnEntity(cloud);
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1.0f, 0.3f);
            if (!user.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }
        return TypedActionResult.success(stack);
    }
}
