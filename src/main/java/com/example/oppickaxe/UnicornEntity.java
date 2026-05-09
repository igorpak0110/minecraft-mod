package com.example.oppickaxe;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class UnicornEntity extends HorseEntity {
    private int poopTime;

    public UnicornEntity(EntityType<? extends UnicornEntity> entityType, World world) {
        super(entityType, world);
        this.poopTime = this.random.nextInt(3000) + 3000;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.getWorld().isClient && this.isAlive() && !this.isBaby()) {
            --this.poopTime;
            if (this.poopTime <= 0) {
                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                        SoundEvents.ENTITY_COW_HURT, this.getSoundCategory(), 1.0f, 0.5f);
                this.dropStack(new ItemStack(OpPickaxeMod.UNICORN_POOP, 1));
                this.poopTime = this.random.nextInt(3000) + 3000;
            }
        }
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {
        super.dropLoot(damageSource, causedByPlayer);
        this.dropStack(new ItemStack(OpPickaxeMod.UNICORN_POOP, this.random.nextBetween(1, 3)));
    }

    public static DefaultAttributeContainer.Builder createUnicornAttributes() {
        return HorseEntity.createBaseHorseAttributes();
    }
}
