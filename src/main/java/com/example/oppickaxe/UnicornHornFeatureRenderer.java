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
import net.minecraft.util.math.RotationAxis;

public class UnicornHornFeatureRenderer extends FeatureRenderer<HorseEntity, HorseEntityModel<HorseEntity>> {
    private static final Identifier HORN_TEXTURE =
            Identifier.of(OpPickaxeMod.MOD_ID, "textures/entity/unicorn_horn.png");

    private final ModelPart horn;

    public UnicornHornFeatureRenderer(FeatureRendererContext<HorseEntity, HorseEntityModel<HorseEntity>> context) {
        super(context);
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        // 2×12×2 pixel horn, UV on 16×16 sheet
        root.addChild("horn",
                ModelPartBuilder.create().uv(0, 0).cuboid(-1f, -12f, -1f, 2, 12, 2),
                ModelTransform.NONE);
        this.horn = TexturedModelData.of(modelData, 16, 16).createModel().getChild("horn");
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, HorseEntity entity, float limbAngle, float limbDistance,
                       float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.isBaby()) return;

        matrices.push();

        // In the feature renderer, the coordinate space is:
        //   1 unit = 1 block, positive-Y = world-DOWN (due to scale(-1,-1,1) in LivingEntityRenderer).
        // The model root sits ~1.5 blocks above entity feet.
        // Horse head-parts pivot is at (0, 15px, -7px) from model root = (0, 0.9375, -0.4375) blocks.
        // Head top (y=-11 from head_parts) is at matrix-Y ≈ 0.25 = world ~1.25 blocks above feet.
        // Translate to just above the forehead so the horn sticks out clearly.
        matrices.translate(0.0f, 0.22f, -0.52f);

        // Follow head rotation (30° base pitch of horse head + dynamic angles)
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(headYaw));
        matrices.multiply(RotationAxis.POSITIVE_X.rotation(
                0.5236f + headPitch * (float) Math.PI / 180f));

        // Scale from block-space to model-pixel-space for the horn geometry
        float s = 1.0f / 16.0f;
        matrices.scale(s, s, s);

        // Render: cuboid base at y=0, tip at y=-12.
        // Negative-Y in pixel-space → less positive in matrix-Y → higher in world → horn points UP.
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(
                RenderLayer.getEntityCutoutNoCull(HORN_TEXTURE));
        horn.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

        matrices.pop();
    }
}
