package com.example.oppickaxe;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.HorseEntityRenderer;

public class ClientInit implements ClientModInitializer {
    @Override
    @SuppressWarnings("unchecked")
    public void onInitializeClient() {
        EntityRendererRegistry.register(OpPickaxeMod.UNICORN,
                ctx -> new HorseEntityRenderer(ctx));
    }
}
