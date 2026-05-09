package com.example.oppickaxe;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.VertexFormat;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

/**
 * Renders a single rainbow arch in the sky during daytime, anchored south of the
 * player and at high altitude. Visible from any angle (cull disabled).
 */
public final class RainbowSkyRenderer {
    private static final Identifier RAINBOW_TEXTURE =
            Identifier.of(OpPickaxeMod.MOD_ID, "textures/sky/rainbow.png");

    private static final float DISTANCE = 280.0f;   // blocks south of camera
    private static final float ALTITUDE = 60.0f;    // blocks above camera
    private static final float WIDTH = 480.0f;      // arch span (blocks)
    private static final float HEIGHT = 240.0f;     // arch peak height (blocks)

    public static void register() {
        WorldRenderEvents.LAST.register(RainbowSkyRenderer::render);
    }

    private static void render(WorldRenderContext context) {
        if (context.world() == null) return;

        // Daytime only. 0..12000 = day, 12000..24000 = night
        long timeOfDay = context.world().getTimeOfDay() % 24000L;
        if (timeOfDay >= 12000L) return;

        MatrixStack matrices = context.matrixStack();
        if (matrices == null) return;

        matrices.push();

        // Position relative to camera (matrix is already in camera space at WorldRenderEvents.LAST).
        // Push the rainbow far in +Z direction (south) and up.
        matrices.translate(0.0f, ALTITUDE, DISTANCE);

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(
                VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        // Quad lying in XY plane, facing -Z (so visible from camera at z=0 looking south).
        // (with cull disabled it renders both sides anyway)
        float halfW = WIDTH / 2.0f;
        buffer.vertex(matrix, -halfW, 0.0f, 0.0f).texture(0.0f, 1.0f);
        buffer.vertex(matrix, halfW, 0.0f, 0.0f).texture(1.0f, 1.0f);
        buffer.vertex(matrix, halfW, HEIGHT, 0.0f).texture(1.0f, 0.0f);
        buffer.vertex(matrix, -halfW, HEIGHT, 0.0f).texture(0.0f, 0.0f);

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, RAINBOW_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false); // don't write to depth (rainbow shouldn't occlude things)

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();

        matrices.pop();
    }
}
