package com.example.oppickaxe;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public final class RainbowSkyRenderer {
    private static final Identifier RAINBOW_TEXTURE =
            Identifier.of(OpPickaxeMod.MOD_ID, "textures/sky/rainbow.png");

    public static void register() {
        WorldRenderEvents.BEFORE_ENTITIES.register(RainbowSkyRenderer::render);
    }

    private static void render(WorldRenderContext context) {
        if (context.world() == null) return;

        MatrixStack matrices = context.matrixStack();
        if (matrices == null) return;

        // DEBUG: render two quads, one at +Z and one at -Z, both close to player.
        // Whichever is visible tells us which Z direction is "in front".
        renderQuad(matrices, 0f, 5f, -30f);  // 30 blocks in -Z, 5 above
        renderQuad(matrices, 0f, 5f, +30f);  // 30 blocks in +Z, 5 above
    }

    private static void renderQuad(MatrixStack matrices, float ox, float oy, float oz) {
        matrices.push();
        matrices.translate(ox, oy, oz);

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float w = 40f, h = 20f;

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buf.vertex(matrix, -w/2, 0, 0).texture(0, 1);
        buf.vertex(matrix,  w/2, 0, 0).texture(1, 1);
        buf.vertex(matrix,  w/2, h, 0).texture(1, 0);
        buf.vertex(matrix, -w/2, h, 0).texture(0, 0);

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, RAINBOW_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();

        BufferRenderer.drawWithGlobalProgram(buf.end());

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();

        matrices.pop();
    }
}
