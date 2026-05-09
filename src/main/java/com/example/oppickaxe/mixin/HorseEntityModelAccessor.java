package com.example.oppickaxe.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Exposes the private {@code head} and {@code body} ModelPart fields of HorseEntityModel
 * so the unicorn horn renderer can attach to the actual head bone (and follow grazing /
 * jumping animations).
 */
@Mixin(HorseEntityModel.class)
public interface HorseEntityModelAccessor {
    @Accessor("head")
    ModelPart getHead();

    @Accessor("body")
    ModelPart getBody();
}
