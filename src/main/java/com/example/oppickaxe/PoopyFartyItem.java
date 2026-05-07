package com.example.oppickaxe;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
            // Launch the player upward
            Vec3d vel = player.getVelocity();
            player.setVelocity(vel.x, 2.5, vel.z);
            player.velocityModified = true;

            // Track this player for continuous smoke while airborne
            OpPickaxeMod.LAUNCHED_PLAYERS.add(player.getUuid());

            // Panda sneeze at low pitch = closest to a fart in vanilla sounds
            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_PANDA_SNEEZE, SoundCategory.PLAYERS, 1.0f, 0.5f);
        }
        return result;
    }
}
