package com.example.oppickaxe;

import net.minecraft.client.render.entity.AbstractHorseEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.entity.passive.HorseEntity;

public class UnicornRenderer extends AbstractHorseEntityRenderer<HorseEntity, HorseEntityModel<HorseEntity>> {
    public UnicornRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new HorseEntityModel<>(ctx.getPart(EntityModelLayers.HORSE)), 0.75f);
        this.addFeature(new UnicornHornFeatureRenderer(this));
    }
}
