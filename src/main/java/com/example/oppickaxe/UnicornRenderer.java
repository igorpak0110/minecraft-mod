package com.example.oppickaxe;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.HorseEntityRenderer;

public class UnicornRenderer extends HorseEntityRenderer {
    public UnicornRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.addFeature(new UnicornHornFeatureRenderer(this));
    }
}
