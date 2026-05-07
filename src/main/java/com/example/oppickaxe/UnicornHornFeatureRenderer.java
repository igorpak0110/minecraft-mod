package com.example.oppickaxe;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.util.Identifier;

public class UnicornHornFeatureRenderer extends FeatureRenderer<HorseEntity, HorseEntityModel<HorseEntity>> {
    private static final Identifier HORN_TEXTURE =
            Identifier.of(OpPickaxeMod.MOD_ID, "textures/entity/unicorn_horn.png");

    private final ModelPart horn;

    public UnicornHornFeatureRenderer(FeatureRendererContext<HorseEntity, HorseEntityModel<HorseEntity>> context) {
        super(context);
        // DEBUG: Big obvious horn — 4×16×4 pixels (0.25×1×0.25 blocks).
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        root.addChild("horn",
                ModelPartBuilder.create().uv(0, 0).cuboid(-2f, -16f, -2f, 4, 16, 4),
                ModelTransform.NONE);
        this.horn = TexturedModelData.of(modelData, 16, 16).createModel().getChild("horn");
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, HorseEntity entity, float limbAngle, float limbDistance,
                       float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.isBaby()) return;

        // DEBUG: render the horn at the model root with NO rotation, scaled to pixel space.
        // If positive matrix-Y = world-DOWN (post-flip), then translating by negative-Y
        // pushes the horn UP in world space. Try -1.5 → 1.5 blocks above model root.
        matrices.push();
        matrices.translate(0.0f, -1.5f, 0.0f);

        float s = 1.0f / 16.0f;
        matrices.scale(s, s, s);

        VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(HORN_TEXTURE));
        horn.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
    }
}
