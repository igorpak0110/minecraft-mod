package com.example.oppickaxe;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class UnicornEntity extends HorseEntity {
    public UnicornEntity(EntityType<? extends UnicornEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {
        super.dropLoot(damageSource, causedByPlayer);
        int count = getRandom().nextBetween(1, 3);
        dropStack(new ItemStack(OpPickaxeMod.UNICORN_POOP, count));
    }

    public static DefaultAttributeContainer.Builder createUnicornAttributes() {
        return HorseEntity.createBaseHorseAttributes();
    }
}
