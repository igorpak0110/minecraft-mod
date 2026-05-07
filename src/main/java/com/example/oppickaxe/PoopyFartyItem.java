package com.example.oppickaxe;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PoopyFartyItem extends Item {
    public PoopyFartyItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack result = super.finishUsing(stack, world, user);
        if (!world.isClient && user instanceof PlayerEntity player) {
            Vec3d vel = player.getVelocity();
            player.setVelocity(vel.x, 2.5, vel.z);
            player.velocityModified = true;
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0));
            // Fart sound (low pitch burp = fart)
            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_COW_HURT, SoundCategory.PLAYERS, 1.0f, 0.3f);
            // Smoke particles at feet
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE,
                        player.getX(), player.getY(), player.getZ(),
                        20, 0.4, 0.1, 0.4, 0.05);
            }
        }
        return result;
    }
}
