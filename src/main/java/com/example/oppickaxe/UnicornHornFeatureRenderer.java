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
        // Horn: 2x12x2 cube, UV starts at (0,0) on 16x16 texture
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
        // Move to approximate horse head position (entity-root space, 1 unit = 1 block)
        // Horse head is ~1.3 blocks up and ~0.4 blocks forward
        matrices.translate(0.0f, 1.35f, -0.45f);

        // Rotate with head so the horn follows where the horse is looking
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-headYaw));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(headPitch));

        // Scale so the model-space units (pixels) map to blocks (1/16 each)
        float s = 1.0f / 16.0f;
        matrices.scale(s, s, s);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(
                RenderLayer.getEntityCutoutNoCull(HORN_TEXTURE));
        horn.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
    }
}
