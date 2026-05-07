package com.example.oppickaxe;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.entity.AreaEffectCloudEntity;

public class FartCloudEntity extends ThrownItemEntity {

    public FartCloudEntity(EntityType<? extends FartCloudEntity> entityType, World world) {
        super(entityType, world);
    }

    public FartCloudEntity(World world, LivingEntity thrower) {
        super(OpPickaxeMod.FART_CLOUD_ENTITY, thrower, world);
    }

    @Override
    protected Item getDefaultItem() {
        return OpPickaxeMod.FART_CLOUD;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(
                    this.getWorld(), this.getX(), this.getY(), this.getZ());
            cloud.setRadius(3.0f);
            cloud.setDuration(200);
            cloud.addEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0));
            cloud.addEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 1));
            this.getWorld().spawnEntity(cloud);
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.ENTITY_PANDA_SNEEZE, SoundCategory.PLAYERS, 1.0f, 0.4f);
            this.discard();
        }
    }
}
