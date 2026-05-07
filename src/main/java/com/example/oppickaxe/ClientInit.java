package com.example.oppickaxe;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class ClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Unicorn uses our custom renderer that adds the horn feature
        EntityRendererRegistry.register(OpPickaxeMod.UNICORN, UnicornRenderer::new);

        // Fart cloud projectile uses the same renderer as snowballs/ender pearls
        EntityRendererRegistry.register(OpPickaxeMod.FART_CLOUD_ENTITY, FlyingItemEntityRenderer::new);
    }
}
