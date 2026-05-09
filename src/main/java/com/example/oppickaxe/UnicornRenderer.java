package com.example.oppickaxe;

import net.minecraft.client.render.entity.AbstractHorseEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.util.Identifier;

public class UnicornRenderer extends AbstractHorseEntityRenderer<HorseEntity, HorseEntityModel<HorseEntity>> {
    // Always render unicorns as white horses (classic unicorn appearance)
    private static final Identifier TEXTURE =
            Identifier.of("minecraft", "textures/entity/horse/horse_white.png");

    public UnicornRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new HorseEntityModel<>(ctx.getPart(EntityModelLayers.HORSE)), 1.1f);
        this.addFeature(new UnicornHornFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(HorseEntity entity) {
        return TEXTURE;
    }
}
